package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

import static org.example.Config.SUCCESSFUL_PATH;
import static org.example.Config.UNSUCCESSFUL_PATH;
import static org.example.HelperPojoJson.convertToPojo;
import static org.example.HelperPojoJson.saveJsonToFile;
import static org.example.SheetsQuickstart.allSavesSuccessful;
import static org.example.SheetsQuickstart.saveOnTable;

public class WorkWithTG extends TelegramLongPollingBot {
    private static final MultiFormatReader reader = new MultiFormatReader();
    private static final Logger logger = Logger.getLogger(SheetsQuickstart.class.getName());
    public Long chatId = null;
    public static List<String> processMessages = new ArrayList<>();

    @Override
    public String getBotUsername() {
        return Config.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

    HelperPojoJson helperPojoJson = new HelperPojoJson();
    ProverkaChekaApi proverkaChekaApi = new ProverkaChekaApi();
    public static String requestJson;

    public String makeResultForSend(String message) throws Exception {
        requestJson = proverkaChekaApi.postMassageAndReturnJson(message);
        saveOnTable(requestJson);
        return convertToPojo(helperPojoJson.parseJson(requestJson));
    }

    @Override
    public void onUpdateReceived(Update update) {
        processMessages = new ArrayList<>();
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (message.hasPhoto()) {
                processMessages.add("Фото обнаружено успешно");
                logger.info("Получено новое сообщение: фото");
                try {
                    handlePhotoMessage(update, message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (message.hasText()) {
                sendMessage(chatId, "Я не знаю что значит : " + message.getText());
            } else {
                sendMessage(chatId, "Неверный формат содержимого ");
            }
        }
    }
    public static long messageId;

    private void handlePhotoMessage(Update update, Message message) throws Exception {
        try {
            BufferedImage image = downloadImage(update);
            processMessages.add("Фото скачано успешно");
            Result result = decodeQR(image);
            processMessages.add("QR код расшифрован успешно");
            if (result != null) {
                SendMessage confirmMassage = new SendMessage(String.valueOf(chatId), "Начинаю обработку чека");//todo переименовать
                Message sentMessage = execute(confirmMassage);
                messageId = sentMessage.getMessageId();

                makeResultForSend(result.getText());
                sendMessage(message.getChatId(), "Все процессы прошли успешно");
                deleteMessage(String.valueOf(chatId), messageId); // Удаляем сообщение
                if (allSavesSuccessful) {
                    HelperPojoJson helperPojoJson = new HelperPojoJson();
                    PojoJson.Root root = helperPojoJson.parseJson(requestJson);

                    // Извлекаем значение check_time
                    String checkTimeString = root.request.manual.check_time;
                    // Приводим строку к правильному формату
                    checkTimeString = checkTimeString.replace('t', 'T');
                    // Парсим строку в LocalDateTime
                    LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

                    String nameOfJsonFile = checkTime.format(DateTimeFormatter.ofPattern("dd_MM_yy__HH_mm")) + "__" + root.request.manual.sum;

                    saveJsonToFile(requestJson, SUCCESSFUL_PATH, nameOfJsonFile);

                    System.out.println("Все данные успешно сохранены во все листы.");
                }

            }
        } catch (Exception e) {
            deleteMessage(String.valueOf(chatId), messageId); // Удаляем сообщение
            HelperPojoJson helperPojoJson = new HelperPojoJson();
            PojoJson.Root root = helperPojoJson.parseJson(requestJson);
            // Извлекаем значение check_time
            String checkTimeString = root.request.manual.check_time;
            // Приводим строку к правильному формату
            checkTimeString = checkTimeString.replace('t', 'T');
            // Парсим строку в LocalDateTime
            LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

            String nameOfJsonFile = root.request.manual.sum + "__" + checkTime.format(DateTimeFormatter.ofPattern("dd_MM_yy__HH_mm"));
            if (!allSavesSuccessful) {
                saveJsonToFile(requestJson, UNSUCCESSFUL_PATH, nameOfJsonFile);
                System.out.println("Сохранение данных завершилось с ошибками. Проверьте логи.");
            }
            // Создаем строку с сообщением об ошибке и стеком вызовов
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element.toString()).append("\n");
            }
            // Выводим строку в терминал
            String infoAboutProcess = "";
            for (String infoMessage : processMessages) {
                infoAboutProcess = infoAboutProcess + "\n" + (infoMessage);
            }
            String errorMessage = infoAboutProcess + "\n" + "\n" + "Произошла ошибка: " + e.getMessage() + "\n" + "\n" + "\n" + stackTrace;
            sendMessage(message.getChatId(), errorMessage);
        }

    }

    public void deleteMessage(String chatId, long messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, Math.toIntExact(messageId));
        try {
            execute(deleteMessage); // Выполняем удаление сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage downloadImage(Update update) throws IOException {
        return Objects.requireNonNull(update.getMessage()).getPhoto()
                .stream()
                .max(Comparator.comparingInt(photoSize -> photoSize.getFileSize()))
                .map(photoSize -> {
                    try {
                        GetFile getFile = new GetFile();
                        getFile.setFileId(photoSize.getFileId());
                        File file = execute(getFile);

                        // Скачиваем файл с сервера Telegram
                        byte[] fileBytes = downloadFileBytes(file);
                        ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
                        BufferedImage image = ImageIO.read(bis);
                        bis.close();
                        return image;
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка при скачивании изображения: ", e);

                    }
                })
                .orElseThrow(() -> new IllegalStateException("Не удалось скачать изображение"));
    }

    private byte[] downloadFileBytes(File file) throws Exception {
        String filePath = String.format("https://api.telegram.org/file/bot%s/%s",
                getBotToken(),
                file.getFilePath());
        URL url = new URL(filePath);
        try (InputStream is = url.openStream()) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new Exception("Неправильный формат сообщения ", e);
        }
    }

    private Result decodeQR(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            return reader.decode(bitmap);
        } catch (NotFoundException e) {
            throw new RuntimeException("Отсутствует qr code", e);
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(chatId.toString());
        sendMessageRequest.setText(message);
        try {
            execute(sendMessageRequest);
            logger.info("Сообщение отправлено в чат: " + chatId);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка при отправке сообщения: ", e);
        }
    }


}