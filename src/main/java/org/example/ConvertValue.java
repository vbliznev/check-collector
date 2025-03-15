package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertValue {

    private static final List<String> STORE_NAMES = Arrays.asList(
            "Магнолия",
            "Красное & Белое",
            "Пятерочка",
            "Дикси",
            "Минимаркет Daily",
            "АШАН",
            "О'КЕЙ",
            "Бэст прайс",
            "Мария-Ра"
    );
    private static final Map<String, String> STORE_NAME_MAP = new HashMap<>();

    static {
        STORE_NAME_MAP.put("АГРОТОРГ", "Пятерочка");
        STORE_NAME_MAP.put("Бэст прай", "Fix Price");
    }

    public static String replaceStoreName(String input) {
        for (String storeName : STORE_NAMES) {
            if (input.contains(storeName)) {
                return storeName;
            }
        }
        for (Map.Entry<String, String> entry : STORE_NAME_MAP.entrySet()) {
            if (input.contains(entry.getKey())) {
                return entry.getValue(); // Возвращаем хорошее название
            }
        }
        return input;
    }

    public static String formatNameOfProduct(String name) {
        return moveToEnd(moveWordsToFront(cleanProductName(name)));
    }

    public static String cleanProductName(String name) {
        return name.replaceAll("БЗМЖ ", "")
                .replaceAll("<А>", "")
                .replaceAll("\\*", "")
                .replaceAll("П.КАФЕ ", "")
                .replaceAll("Пивной напиток ", "")
                .replaceAll("КД/", "")
                .replaceAll("СПз ", "")
                .replaceAll("СПз ", "");
    }

    private static final List<String> words = Arrays.asList("Сыр", "Молоко", "Лаваш", "Онигири", "Вино");

    private static String moveWordsToFront(String input) {
        StringBuilder result = new StringBuilder();
        StringBuilder remaining = new StringBuilder(input);

        for (String word : words) {
            String wordWithSpace = word + " ";
            while (remaining.toString().contains(wordWithSpace)) {
                // Удаляем слово из remaining и добавляем его в начало result
                remaining = new StringBuilder(remaining.toString().replaceFirst(wordWithSpace, ""));
                result.insert(0, wordWithSpace);
            }
        }
        // Добавляем оставшуюся часть строки
        result.append(remaining.toString().trim());
        return result.toString();
    }

    private static String moveToEnd(String input) {
        int index = -1;
        for (int i = 0; i < input.length(); i++) {
            if (Character.UnicodeScript.of(input.charAt(i)) == Character.UnicodeScript.CYRILLIC) {
                index = i;
                break;
            }
        }
        // Перемещаем кириллические символы в конец
        return (index != -1) ? input.substring(index) + " " + input.substring(0, index).trim() : input;
    }

}
