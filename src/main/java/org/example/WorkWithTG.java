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
import org.example.depricated.SheetsQuickstart;
import org.example.pojo.HelperPojoJson;
import org.example.pojo.PojoJson;
import org.example.pojo.ProductWithTransaction;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.Config.SUCCESSFUL_PATH;
import static org.example.CreateCSV.getAllProductsAsCsv;
import static org.example.DBHElper.saveOnDB;
import static org.example.pojo.HelperPojoJson.saveJsonToFile;
import static org.example.pojo.ProductWithTransactionHelper.convertRootsToProductWithTransactions;

public class WorkWithTG extends TelegramLongPollingBot {
    private static final MultiFormatReader reader = new MultiFormatReader();
    static final Logger logger = Logger.getLogger(SheetsQuickstart.class.getName());
    public static Long chatId = null;
    public static List<String> processMessages = new ArrayList<>();

    @Override
    public String getBotUsername() {
        return Config.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

    public static String requestJson;

    ProverkaChekaApi proverkaChekaApi = new ProverkaChekaApi();

    static HelperPojoJson helperPojoJson = new HelperPojoJson();

    String regex = "t=\\d{8}T\\d{4}&s=\\d+\\.\\d{2}&fn=\\d{16}&i=\\d{1,10}&fp=\\d{1,10}&n=[12]";
    Pattern pattern = Pattern.compile(regex);

    public void sendInfoAboutStartWork() throws Exception {
        SendMessage confirmMassage = new SendMessage(String.valueOf(chatId), "Начинаю обработку чека");
        Message sentMessage = execute(confirmMassage);
        messageId = sentMessage.getMessageId();
    }

    public void getInfoAboutCheck(String message) throws Exception {
        sendInfoAboutStartWork();
        PojoJson.Root root = proverkaChekaApi.getInformationAboutCheckAndReturnPOJO(message);
        returnStringAboutCheck(root);
    }

    public void returnStringAboutCheck(PojoJson.Root root) throws Exception {
        List<ProductWithTransaction> newProducts = convertRootsToProductWithTransactions(root);
        saveOnDB(newProducts);
        sendMessage(chatId, "Все процессы прошли успешно");
        deleteMessage(String.valueOf(chatId), messageId); // Удаляем сообщение
//        if (allSavesSuccessful) {
//            saveFailJson(newProducts);
//        }
    }

    public static void saveFailJson(List<ProductWithTransaction> newProducts) throws Exception {
        // Извлекаем значение check_time
        String checkTimeString = String.valueOf(newProducts.get(0).getDate());
        // Парсим строку в LocalDateTime
        LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        String nameOfJsonFile = checkTime.format(DateTimeFormatter.ofPattern("dd_MM_yy__HH_mm")) + "__" + newProducts.get(0).getAmount();

        saveJsonToFile(requestJson, SUCCESSFUL_PATH, nameOfJsonFile);

        System.out.println("Файл сохранен");
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            try {
                switch (getMessageType(message)) {
                    case PHOTO:
                        processPhotoMessage(update, message);
                        break;
                    case TEXT:
                        processTextMessage(message);
                        break;
                    default:
                        sendMessage(chatId, "Неверный формат содержимого");
                        break;
                }
            } catch (Exception e) {
                sendMessage(chatId, "Произошла ошибка при обработке вашего сообщения.");
                logger.info("Ошибка при обработке сообщения:"+e);

                throw new RuntimeException("Ошибка при обработке сообщения: ", e);
            }
        }
    }

    private MessageType getMessageType(Message message) {
        if (message.hasPhoto()) {
            return MessageType.PHOTO;
        } else if (message.hasText()) {
            return MessageType.TEXT;
        } else {
            return MessageType.UNKNOWN;
        }
    }

    private void processPhotoMessage(Update update, Message message) throws Exception {
        logger.info("Получено новое сообщение: фото");
        getInfoFromQr(update, message);
    }

    private void processTextMessage(Message message) throws Exception {
        String text = message.getText();
        Matcher matcher = pattern.matcher(text);
        if (text != null) {
            if (text.toLowerCase().contains("csv")) {
                sendCsvToTelegram();
            } else if (matcher.find()) {
                getInfoAboutCheck(matcher.group());
            } else {
                sendMessage(chatId, "Я не знаю что значит: " + text);
                throw new RuntimeException("Совпадение не найдено.");
            }
        } else {
            sendMessage(chatId, "Я не могу найти нужную информацию");
        }
    }

    private enum MessageType {
        PHOTO,
        TEXT,
        UNKNOWN
    }

    public static long messageId;

    private void getInfoFromQr(Update update, Message message) throws Exception {
        BufferedImage image = downloadImage(update);
        processMessages.add("Фото скачано успешно");
        Result result = decodeQR(image);
        processMessages.add("QR код расшифрован успешно");
        getInfoAboutCheck(result.getText());
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
    public void sendCsvToTelegram() {
        String csvData = getAllProductsAsCsv();

        if (csvData == null || csvData.isEmpty()) {
            System.err.println("Ошибка: данные CSV пустые!");
            return;
        }

        try {
            // Преобразуем строку CSV в поток байтов
            ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes());

            // Создаем объект InputFile
            InputFile inputFile = new InputFile();
            inputFile.setMedia(inputStream, "products.csv");

            // Создаем объект SendDocument
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(chatId));
            sendDocument.setDocument(inputFile);

            // Отправляем документ
            execute(sendDocument);
            System.out.println("CSV файл успешно отправлен в Telegram!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }

}


