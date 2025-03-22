package org.example.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class ProductWithTransaction {
    private String name;      // Название товара
    private double price;     // Цена товара
    private float quantity;   // Количество товара
    private double total;     // Сумма (price * quantity)
    private String storeName; // Название магазина
    private LocalDateTime date; // Дата транзакции
    private double amount;    // Сумма транзакции

    public ProductWithTransaction(String name, double price, float quantity, String storeName, LocalDateTime date) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = price * quantity; // Вычисляем сумму
        this.storeName = storeName;
        this.date = date;
        this.amount = total; // Сумма транзакции равна общей сумме товара
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return roundToTwoDecimalPlaces(price);
    }

    public float getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return roundToTwoDecimalPlaces(total);
    }

    public String getStoreName() {
        return storeName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAmount() {
        return roundToTwoDecimalPlaces(amount);
    }

    // Метод для округления до двух знаков после запятой
    private double roundToTwoDecimalPlaces(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // Сеттеры для транзакции
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}