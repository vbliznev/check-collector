package org.example;

import java.time.LocalDateTime;

public class StoreTransaction {
    private String storeName; // Название магазина
    private LocalDateTime date;    // Дата транзакции
    private double amount;      // Сумма транзакции

    // Конструктор
    public StoreTransaction(String storeName, LocalDateTime date, double amount) {
        this.storeName = storeName;
        this.date = date;
        this.amount = amount;
    }

    // Геттеры
    public String getStoreName() {
        return storeName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getTotal() {
        return amount;
    }

    // Сеттеры
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "StoreTransaction{" +
                "storeName='" + storeName + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}