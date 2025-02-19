package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class HelperProduct {

    // Метод для преобразования строки в экземпляр класса Product
    static Product convertRowToProduct(List<Object> row) {
        String name = (String) row.get(0);

        // Заменяем запятую на точку для корректного преобразования
        String priceString = ((String) row.get(1)).replace(',', '.');
        double price = Double.parseDouble(priceString);

        String quantityString = ((String) row.get(2)).replace(',', '.');
        float quantity = Float.parseFloat(quantityString);

        return new Product(name, price, quantity);
    }

    public static List<Product> groupProducts(List<Product> products) {
        Map<String, Product> groupedProducts = new HashMap<>();

        for (Product product : products) {
            String name = product.getName();
            if (groupedProducts.containsKey(name)) {
                // Если продукт уже существует, обновляем его данные
                Product existingProduct = groupedProducts.get(name);
                existingProduct.quantity += product.getQuantity(); // Увеличиваем количество
                existingProduct.total += product.getTotal(); // Суммируем общую стоимость
            } else {
                // Если продукт новый, добавляем его в карту
                groupedProducts.put(name, new Product(name, product.getPrice(), product.getQuantity()));
                groupedProducts.get(name).total = product.getTotal(); // Устанавливаем начальную сумму
            }
        }

        // Преобразуем карту обратно в список
        List<Product> result = new ArrayList<>();
        for (Product groupedProduct : groupedProducts.values()) {
            // Пересчитываем цену
            groupedProduct.price = groupedProduct.total / groupedProduct.quantity;
            result.add(groupedProduct);
        }
        formatNames(result);
        return result;
    }

    public static void sortProductsByName(List<Product> products) {
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        });
    }

    public static List<Product> formatNames(List<Product> products) {
        return products.stream()
                .map(product -> new Product(
                        moveToEnd(moveWordsToFront(product.getName().replaceAll("\\*", "") // Удаляем звездочки и создаем новый объект
                                .replaceAll("БЗМЖ ", "")
                                .replaceAll("<А>", "")
                                .replaceAll("КД/", "")
                                .replaceAll("СПз ", ""))),
                        product.getPrice(),
                        product.getQuantity()))
                .collect(Collectors.toList());
    }

    public static List<String> words = new ArrayList<>(Arrays.asList("Сыр", "Молоко", "Лаваш", "Онигири"));

    public static String moveWordsToFront(String input) {
        StringBuilder result = new StringBuilder();
        StringBuilder remaining = new StringBuilder(input);

        for (String word : words) {
            String wordWithSpace = word + " ";
            while (remaining.toString().contains(wordWithSpace)) {
                // Удаляем слово и пробел из оставшейся строки
                remaining = new StringBuilder(remaining.toString().replaceFirst(wordWithSpace, ""));
                // Добавляем слово в начало результата
                result.insert(0, wordWithSpace);
            }
        }

        // Добавляем оставшуюся часть строки
        result.append(remaining.toString().trim());
        return result.toString();
    }

    public static String moveToEnd(String input) {
        // Находим индекс первого кириллического символа
        int index = -1;
        for (int i = 0; i < input.length(); i++) {
            if (Character.UnicodeScript.of(input.charAt(i)) == Character.UnicodeScript.CYRILLIC) {
                index = i;
                break;
            }
        }
        // Если кириллический символ найден, формируем новую строку
        if (index != -1) {
            String before = input.substring(0, index); // Символы до кириллического
            String after = input.substring(index); // Символы начиная с кириллического
            return after + " " + before; // Перемещаем символы в конец
        }

        // Если кириллический символ не найден, возвращаем исходную строку
        return input;
    }

    public static Double calculateTotalSum(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getTotal) // Получаем total для каждого продукта
                .sum(); // Суммируем все total
    }

    public static Double calculateTotalSum(List<Product> products, List<Product> products1) {
        products.addAll(products1);
        return products.stream()
                .mapToDouble(Product::getTotal) // Получаем total для каждого продукта
                .sum(); // Суммируем все total
    }


}
