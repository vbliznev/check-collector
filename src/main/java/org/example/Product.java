package org.example;

import java.text.DecimalFormat;

public class Product {
    private String name;      // Название товара
    double price;            // Цена товара
    float quantity;          // Количество товара
    double total;            // Сумма (price * quantity)

    // Конструктор
    public Product(String name, double price, float quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity; // Вычисляем сумму
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public float getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + formatFlot(price) +
                ", quantity=" + formatFlot(quantity) +
                ", total=" + formatFlot(total) +
                '}';
    }


    public static double formatFlot(Object price) {
        try {
            // Удаляем все пробелы и запятые из строки
            String s = price.toString().replaceAll("[ ,]", "");

            // Преобразуем строку в число
            double number = Double.parseDouble(s);

            // Форматируем число с двумя знаками после запятой
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedNumber = decimalFormat.format(number).replace(",", ".");

            // Возвращаем отформатированное число как double
            return Double.parseDouble(formattedNumber);
        } catch (NumberFormatException e) {
            // Обработка ошибки, если входные данные некорректны
            System.err.println("Ошибка: Невозможно преобразовать входные данные в число.");
            return 0.0; // Возвращаем значение по умолчанию
        }
    }

}