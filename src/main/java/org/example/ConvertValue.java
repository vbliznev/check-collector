package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertValue {

    // Список названий магазинов
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

    // Маппинг для замены некорректных названий магазинов
    private static final Map<String, String> STORE_NAME_MAP = new HashMap<>();

    static {
        STORE_NAME_MAP.put("АГРОТОРГ", "Пятерочка");
        STORE_NAME_MAP.put("Бэст прай", "Fix Price");
    }

    // Метод для замены названия магазина в строке
    public static String replaceStoreName(String input) {
        for (String storeName : STORE_NAMES) {
            if (input.contains(storeName)) {
                return storeName;
            }
        }
        for (Map.Entry<String, String> entry : STORE_NAME_MAP.entrySet()) {
            if (input.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return input;
    }

    // Метод для форматирования названия продукта
    public static String formatNameOfProduct(String name) {
        String cleanedName = cleanProductName(name);
        String movedWords = moveWordsToFront(cleanedName);
        return moveToEnd(movedWords);
    }

    // Массив строк, которые нужно удалить из названия продукта
    private static final List<String> STRINGS_TO_REMOVE = Arrays.asList(
            "БЗМЖ ",
            "<А>",
            "\\*",
            "П.КАФЕ ",
            "Пивной напиток ",
            "КД/",
            "СПз "
    );

    // Метод для очистки названия продукта от лишних символов и слов
    public static String cleanProductName(String name) {
        String result = name;
        for (String str : STRINGS_TO_REMOVE) {
            result = result.replaceAll(str, "");
        }
        return result;
    }


    // Список ключевых слов, которые нужно переместить в начало
    private static final List<String> KEYWORDS = Arrays.asList("Сыр", "Молоко", "Лаваш", "Онигири", "Вино");

    // Метод для перемещения ключевых слов в начало строки
    private static String moveWordsToFront(String input) {
        StringBuilder result = new StringBuilder();
        StringBuilder remaining = new StringBuilder(input);

        for (String word : KEYWORDS) {
            String wordWithSpace = word + " ";
            while (remaining.toString().contains(wordWithSpace)) {
                remaining = new StringBuilder(remaining.toString().replaceFirst(wordWithSpace, ""));
                result.insert(0, wordWithSpace);
            }
        }
        result.append(remaining.toString().trim());
        return result.toString();
    }

    // Метод для перемещения кириллических символов в конец строки
    private static String moveToEnd(String input) {
        int index = -1;
        for (int i = 0; i < input.length(); i++) {
            if (Character.UnicodeScript.of(input.charAt(i)) == Character.UnicodeScript.CYRILLIC) {
                index = i;
                break;
            }
        }
        return (index != -1) ? input.substring(index) + " " + input.substring(0, index).trim() : input;
    }
}
