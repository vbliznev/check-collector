package org.example;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.example.pojo.PojoJson;
import org.example.pojo.ProductWithTransaction;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.CreateCSV.getAllProductsAsCsv;
import static org.example.DBHelper.saveOnDB;
import static org.example.pojo.ProductWithTransactionHelper.convertRootsToProductWithTransactions;

public class WorkWithTG extends TelegramLongPollingBot {

    // Константы и зависимости
    private static final MultiFormatReader QR_READER = new MultiFormatReader();
    public static Logger logger = Logger.getLogger("Logger");
    private static final String QR_REGEX = "t=\\d{8}T\\d{4}&s=\\d+\\.\\d{2}&fn=\\d{16}&i=\\d{1,10}&fp=\\d{1,10}&n=[12]";
    private static final Pattern QR_PATTERN = Pattern.compile(QR_REGEX);
    public static Long chatId = null;
    private static long messageId;

    private final ProverkaChekaApi proverkaChekaApi = new ProverkaChekaApi();

    // Реализация методов TelegramLongPollingBot
    @Override
    public String getBotUsername() {
        return Config.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Config.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId();
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
                handleError(e);
            }
        }
    }

    // Обработка ошибок
    private void handleError(Exception e) {
        sendMessage(chatId, "Произошла ошибка при обработке вашего сообщения.");
        logger.severe("Ошибка при обработке сообщения: " + e.getMessage());
    }

    // Определение типа сообщения
    private MessageType getMessageType(Message message) {
        if (message.hasPhoto()) {
            return MessageType.PHOTO;
        } else if (message.hasText()) {
            return MessageType.TEXT;
        } else {
            return MessageType.UNKNOWN;
        }
    }

    // Обработка фото-сообщений
    private void processPhotoMessage(Update update, Message message) throws Exception {
        logger.info("Получено новое сообщение: фото");
        BufferedImage image = downloadImage(update);
        logger.info("Фото скачано успешно");
        Result qrResult = decodeQR(image);
        logger.info("QR код расшифрован успешно");
        getInfoAboutCheck(qrResult.getText());
    }

    // Обработка текстовых сообщений
    private void processTextMessage(Message message) throws Exception {
        String text = message.getText();
        Matcher matcher = QR_PATTERN.matcher(text);
        if (text != null) {
            if (text.toLowerCase().contains("csv")) {
                sendCsvToTelegram();
            } else if (matcher.find()) {
                getInfoAboutCheck(matcher.group());
            } else if (text.toLowerCase().contains("start")) {
                sendMessage(chatId, "Привет, давай начнем");
            } else {
                sendMessage(chatId, "Я не знаю что значит: " + text);
            }
        }
    }

    // Работа с чеками
    public void getInfoAboutCheck(String message) throws Exception {
        sendInfoAboutStartWork();
        PojoJson.Root root = proverkaChekaApi.getInformationAboutCheckAndReturnPOJO(message);
        returnStringAboutCheck(root);
    }

    private void returnStringAboutCheck(PojoJson.Root root) {
        List<ProductWithTransaction> newProducts = convertRootsToProductWithTransactions(root);
        saveOnDB(newProducts);
        sendMessage(chatId, "Все процессы прошли успешно");
        deleteMessage(String.valueOf(chatId), messageId);
    }

    // Уведомление о начале работы
    public void sendInfoAboutStartWork() throws Exception {
        SendMessage confirmMessage = new SendMessage(String.valueOf(chatId), "Начинаю обработку чека");
        Message sentMessage = execute(confirmMessage);
        messageId = sentMessage.getMessageId();
    }

    // Удаление сообщений
    public void deleteMessage(String chatId, long messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, Math.toIntExact(messageId));
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            logger.severe("Ошибка при удалении сообщения: " + e.getMessage());
        }
    }

    // Загрузка изображения
    private BufferedImage downloadImage(Update update) throws IOException {
        return update.getMessage().getPhoto().stream()
                .max(Comparator.comparingInt(PhotoSize::getFileSize))
                .map(photoSize -> {
                    try {
                        GetFile getFile = new GetFile();
                        getFile.setFileId(photoSize.getFileId());
                        File file = execute(getFile);
                        byte[] fileBytes = downloadFileBytes(file);
                        return ImageIO.read(new ByteArrayInputStream(fileBytes));
                    } catch (Exception e) {
                        throw new RuntimeException("Ошибка при скачивании изображения: ", e);
                    }
                })
                .orElseThrow(() -> new IllegalStateException("Не удалось скачать изображение"));
    }

    // Загрузка файла по URL
    private byte[] downloadFileBytes(File file) throws Exception {
        String filePath = String.format("https://api.telegram.org/file/bot%s/%s", getBotToken(), file.getFilePath());
        try (InputStream is = new URL(filePath).openStream()) {
            return is.readAllBytes();
        }
    }

    // Декодирование QR-кода
    private Result decodeQR(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            return QR_READER.decode(bitmap);
        } catch (NotFoundException e) {
            throw new RuntimeException("Отсутствует QR-код", e);
        }
    }

    // Отправка сообщений
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

    // Отправка CSV-файла
    public void sendCsvToTelegram() {
        String csvData = getAllProductsAsCsv();
        if (csvData == null || csvData.isEmpty()) {
            logger.severe("Ошибка: данные CSV пустые!");
            return;
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(csvData.getBytes())) {
            InputFile inputFile = new InputFile();
            inputFile.setMedia(inputStream, "products.csv");

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(chatId));
            sendDocument.setDocument(inputFile);

            execute(sendDocument);
            logger.info("CSV файл успешно отправлен в Telegram!");
        } catch (Exception e) {
            logger.severe("Ошибка при отправке CSV: " + e.getMessage());
        }
    }

    // Перечисление типов сообщений
    private enum MessageType {
        PHOTO,
        TEXT,
        UNKNOWN
    }
}