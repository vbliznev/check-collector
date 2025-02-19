package org.example;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class HelperStoreTransaction {

    public static StoreTransaction convertRootsToStoreTransaction(PojoJson.Root root) {
        List<StoreTransaction> transactions = new ArrayList<>();

        String retailPlace = root.data.json.user + " - " + root.data.json.retailPlace;
        retailPlace = retailPlace
                .replace("ООО", "")
                .replace("ОБЩЕСТВО С ОГРАНИЧЕННОЙ ОТВЕТСТВЕННОСТЬЮ", "");
        retailPlace = retailPlace.replaceAll("\\s+", " ").trim();

        Double ecashTotalSum = Double.valueOf((root.data.json.ecashTotalSum / 100) + "." + (root.data.json.ecashTotalSum % 100));

        // Извлекаем значение check_time
        String checkTimeString = root.request.manual.check_time;
        // Приводим строку к правильному формату
        checkTimeString = checkTimeString.replace('t', 'T');
        // Парсим строку в LocalDateTime
        LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        // Создаем новый объект StoreTransaction и добавляем его в список
            StoreTransaction storeTransaction = new StoreTransaction(retailPlace, checkTime, ecashTotalSum);
            transactions.add(storeTransaction);

        return storeTransaction;
    }

    public static void sortTransactionsByDate(List<StoreTransaction> transactions) {
        Collections.sort(transactions, new Comparator<StoreTransaction>() {
            @Override
            public int compare(StoreTransaction t1, StoreTransaction t2) {
                return t1.getDate().compareTo(t2.getDate()); // Сравниваем даты
            }
        });
    }
}