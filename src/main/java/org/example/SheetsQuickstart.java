package org.example;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

import static org.example.Config.*;
import static org.example.HelperProduct.*;
import static org.example.HelperStoreTransaction.convertRootsToStoreTransaction;
import static org.example.HelperStoreTransaction.sortTransactionsByDate;
import static org.example.Product.*;


public class SheetsQuickstart {
    private static final Logger logger = Logger.getLogger(SheetsQuickstart.class.getName());
    private static HttpTransport httpTransport;
    private static Sheets service;

    static {
        try {
            // Инициализация HTTP-транспорта
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            // Инициализация сервиса Google Sheets
            service = getSheetsService(httpTransport);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Ошибка инициализации: ", e);
        }
    }

    public static boolean allSavesSuccessful = true; // Флаг для отслеживания успешности сохранений

    public static void saveOnTable(String requestJson) {
        try {
            HelperPojoJson helperPojoJson = new HelperPojoJson();
            PojoJson.Root root = helperPojoJson.parseJson(requestJson);

            // Обработка продуктов
            List<Product> newProducts = convertRootsToProducts(root);
            List<Product> existingProducts = getProductsFromSpreadsheet();
            List<Product> allProducts = mergeAndFormatProducts(newProducts, existingProducts);

//            if (!isTotalSumValid(newProducts, existingProducts, allProducts)) {
//                throw new RuntimeException("Контрольные суммы не совпали");
//            }

            // Обработка транзакций
            StoreTransaction newTransaction = convertRootsToStoreTransaction(root);
            List<StoreTransaction> allTransactions = getStoreTransactionsFromSpreadsheet();
            allTransactions.add(newTransaction);
            sortTransactionsByDate(allTransactions);

            // Сохранение данных
            saveProductsToSheet(allProducts);
            saveTransactionsToSheet(allTransactions);
            saveBackupData(newProducts, newTransaction);

            logger.info("Данные успешно сохранены.");
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении данных: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static List<Product> mergeAndFormatProducts(List<Product> newProducts, List<Product> existingProducts) {
        List<Product> allProducts = new ArrayList<>();
        allProducts.addAll(newProducts);
        allProducts.addAll(existingProducts);
        return formatNames(groupProducts(allProducts));
    }

    private static boolean isTotalSumValid(List<Product> newProducts, List<Product> existingProducts, List<Product> allProducts) {
        double newSum = calculateTotalSum(newProducts);
        double existingSum = calculateTotalSum(existingProducts);
        double allSum = calculateTotalSum(allProducts);

        if (newSum + existingSum != allSum) {
            logSumMismatch(newProducts, existingProducts, allProducts);
            return false;
        }
        return true;
    }

    private static void saveDataToSpreadsheet(String list, List<List<Object>> rows) throws IOException {
        // Найти первую пустую строку в указанном диапазоне
        int firstEmptyRow = findFirstEmptyRow(list + "!A1:A");

        // Вставляем данные в таблицу
        ValueRange body = new ValueRange()
                .setMajorDimension("ROWS")
                .setValues(rows);
        try {
            service.spreadsheets().values()
                    .update(SPREADSHEET_ID, list + "!A" + firstEmptyRow, body)
                    .setValueInputOption("RAW")
                    .execute();
            logger.info("Данные успешно сохранены в диапазон: " + list);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении данных в диапазон: " + list, e);
        }

    }

    private static List<List<Object>> prepareDataForSaveToBuying(List<StoreTransaction> listStoreTransactionsFromSheets) throws IOException {
        List<List<Object>> rows = new ArrayList<>();

        // Проходим по всем элементам списка и добавляем их в rows
        for (StoreTransaction transaction : listStoreTransactionsFromSheets) {
            List<Object> row = new ArrayList<>();
            row.add(transaction.getStoreName()); // Название магазина
            row.add(String.valueOf(transaction.getDate()).replace("T", " ")); // Дата
            row.add(String.valueOf(transaction.getTotal()).replace('.', ',')); // Общая сумма
            rows.add(row);
        }

        // Вставляем данные в таблицу
        return rows;
    }

    private static List<List<Object>> prepareDataForSaveToBackup(List<Product> listPogo, StoreTransaction storeTransactionsFromJson) throws IOException {
        List<List<Object>> rows = new ArrayList<>();

        // Проходим по всем элементам списка и добавляем их в rows
        for (Product product : listPogo) {
            List<Object> row = new ArrayList<>();
            row.add(storeTransactionsFromJson.getStoreName()); // Название магазина
            row.add(String.valueOf(storeTransactionsFromJson.getDate()).replace("T", " ")); // Дата
            row.add(formatFlot(storeTransactionsFromJson.getTotal())); // Общая сумма
            row.add(String.valueOf(product.getName())); // Название продукта
            row.add(formatFlot(product.getPrice())); // Цена продукта
            row.add(formatFlot(product.getQuantity())); // Количество продукта
            row.add(formatFlot(product.getTotal())); // Сумма по продукту
            rows.add(row);
        }
        return rows;

    }

    private static void applyUnderlineToRow(int row) throws IOException {
        BatchUpdateSpreadsheetRequest request = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request()
                        .setUpdateBorders(new UpdateBordersRequest()
                                .setRange(new GridRange()
                                        .setSheetId(getSheetId(BACKUP_SHEET_NAME))
                                        .setStartRowIndex(row - 1) // Указываем строку, которую хотим подчеркнуть
                                        .setEndRowIndex(row) // Конечный индекс строки
                                        .setStartColumnIndex(0)
                                        .setEndColumnIndex(7))
                                .setTop(new Border().setStyle("SOLID").setWidth(1)) // Устанавливаем верхнюю границу
                        )));

        service.spreadsheets().batchUpdate(SPREADSHEET_ID, request).execute();
    }

    private static int getSheetId(String sheetName) throws IOException {
        return service.spreadsheets().get(SPREADSHEET_ID).execute()
                .getSheets().stream()
                .filter(sheet -> sheet.getProperties().getTitle().equals(sheetName))
                .findFirst()
                .orElseThrow(() -> new IOException("Лист не найден: " + sheetName))
                .getProperties().getSheetId();
    }

    private static void logSumMismatch(List<Product> newProducts, List<Product> existingProducts, List<Product> allProducts) {
        logger.warning("Контрольные суммы не совпали:");
        logger.warning("Новые данные: " + calculateTotalSum(newProducts));
        logger.warning("Данные из таблицы: " + calculateTotalSum(existingProducts));
        logger.warning("Результирующая сумма: " + calculateTotalSum(allProducts));
    }

    private static void saveProductsToSheet(List<Product> products) throws IOException {
        List<List<Object>> dataForSave = prepareRowsToInsertProducts(products);
        clearTableExceptFirstRow("Продукты!A2:D");
        saveDataToSpreadsheet("Продукты", dataForSave);
        logger.info("Данные сохранены в лист 'Продукты'.");
    }

    private static void saveTransactionsToSheet(List<StoreTransaction> transactions) throws IOException {
        List<List<Object>> dataForSave = prepareDataForSaveToBuying(transactions);
        clearTableExceptFirstRow(PURCHASES_SHEET_NAME + "!A2:C");
        saveDataToSpreadsheet(PURCHASES_SHEET_NAME, dataForSave);
        logger.info("Данные сохранены в лист 'Покупки'.");
    }

    private static void saveBackupData(List<Product> products, StoreTransaction transaction) throws IOException {
        List<List<Object>> dataForSave = prepareDataForSaveToBackup(products, transaction);
        saveDataToSpreadsheet(BACKUP_SHEET_NAME, dataForSave);
        applyUnderlineToRow(findFirstEmptyRow(BACKUP_SHEET_NAME + "!A1:A"));
        logger.info("Данные сохранены в лист 'Бэкап'.");
    }

    private static Sheets getSheetsService(HttpTransport httpTransport) throws IOException {
        // Получаем учетные данные из файла сервисного аккаунта
        InputStream in = SheetsQuickstart.class.getResourceAsStream(SERVICE_ACCOUNT_FILE_PATH);
        if (in == null) {
            throw new RuntimeException("Ресурс не найден: " + SERVICE_ACCOUNT_FILE_PATH);
        }

        GoogleCredential credential = GoogleCredential.fromStream(in)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        // Создаем и возвращаем экземпляр сервиса Google Sheets
        return new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static void clearTableExceptFirstRow(String range) throws IOException {
        service.spreadsheets().values().clear(SPREADSHEET_ID, range, new ClearValuesRequest()).execute();
        logger.info("Диапазон " + range + " очищен.");
    }

    private static int findFirstEmptyRow(String range) throws IOException {
        ValueRange valueRange = service.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
        return valueRange.getValues().size() + 1;
    }

    private static List<List<Object>> prepareRowsToInsertProducts(List<Product> products) {
        List<List<Object>> rows = new ArrayList<>();

        // Prepare rows for insertion
        for (Product product : products) {
            List<Object> row = new ArrayList<>();
            row.add(product.getName());
            row.add(formatFlot(String.valueOf(product.getPrice())));
            row.add(formatFlot(String.valueOf(product.getQuantity())));
            row.add(formatFlot(String.valueOf(product.getTotal())));
            rows.add(row);
        }
        logger.info("Подготовлено " + rows.size() + " строк для вставки продуктов.");
        return rows;
    }


    private static void insertRowsIntoSpreadsheet(String range, List<List<Object>> rows) throws IOException {
        ValueRange body = new ValueRange()
                .setMajorDimension("ROWS")
                .setValues(rows);

        AppendValuesResponse response = service.spreadsheets().values()
                .append(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
        if (range.contains("Продукты")) {
            logger.info(response.getUpdates().getUpdatedCells() + " ячеек добавлено в лиcт 'Продукты'");
        } else if (range.contains("Покупки")) {
            logger.info(response.getUpdates().getUpdatedCells() + " ячеек добавлено в лиcт 'Покупки'");
        } else if (range.contains("Бэкап")) {
            logger.info(response.getUpdates().getUpdatedCells() + " ячеек добавлено в лиcт 'Бэкап'");
        } else {
            logger.info("Ошибка при добавлении данны[ в лист " + range);

        }
    }

    // Метод для извлечения продуктов из таблицы
    public static List<Product> getProductsFromSpreadsheet() throws IOException {
        // Получаем данные из таблицы
        ValueRange valueRange = service.spreadsheets().values()
                .get(SPREADSHEET_ID, "A2:D") // Предполагаем, что данные начинаются со второй строки
                .execute();
        List<List<Object>> values = valueRange.getValues();

        List<Product> products = new ArrayList<>();

        if (values != null) {
            for (List<Object> row : values) {
                if (row.size() >= 4) { // Убедимся, что в строке достаточно данных
                    Product product = convertRowToProduct(row);
                    products.add(product);
                }
            }
        }

        logger.info("Извлечено " + products.size() + " продуктов из листа 'Продукты'.");


        logger.info("Извлечено " + products.size() + " продуктов из таблицы.");
        return products;
    }


    public static List<StoreTransaction> getStoreTransactionsFromSpreadsheet() throws IOException {
        // Указываем диапазон для получения данных из листа "Покупки"
        String range = "'" + PURCHASES_SHEET_NAME + "'!A2:C"; // Используем название листа для указания диапазона

        // Получаем данные из таблицы
        ValueRange valueRange = service.spreadsheets().values()
                .get(SPREADSHEET_ID, range) // Получаем данные из указанного диапазона
                .execute();
        List<List<Object>> values = valueRange.getValues();

        List<StoreTransaction> transactions = new ArrayList<>();

        if (values != null) {
            for (List<Object> row : values) {
                if (row.size() >= 3) { // Убедимся, что в строке достаточно данных
                    StoreTransaction transaction = convertRowToStoreTransaction(row);
                    transactions.add(transaction);
                }
            }
            logger.info("Извлечено " + transactions.size() + " транзакций из листа 'Покупки'.");
        } else {
            logger.warning("Нет данных для извлечения из листа 'Покупки'.");
        }

        return transactions;
    }

    public static List<Product> convertRootsToProducts(PojoJson.Root root) {
        List<Product> products = new ArrayList<>();
        // Предполагаем, что у каждого root есть доступ к items
        for (PojoJson.Item item : root.data.json.items) {
            // Создаем новый объект Product из каждого item
            Product product = new Product(item.name, item.price / 100, item.quantity);
            products.add(product);
        }

        logger.info("Конвертировано " + products.size() + " продуктов из JSON.");
        return products;
    }

    // Метод для преобразования строки в StoreTransaction
    private static StoreTransaction convertRowToStoreTransaction(List<Object> row) {
        String storeName = row.get(0).toString(); // Название магазина
        LocalDateTime date = LocalDateTime.parse(row.get(1).toString().replace(" ", "T")); // Дата транзакции
        double amount = Double.parseDouble(row.get(2).toString().replace(",", ".")); // Сумма транзакции

        logger.info("Преобразована транзакция: " + storeName + ", Дата: " + date + ", Сумма: " + amount);
        return new StoreTransaction(storeName, date, amount);
    }

}



