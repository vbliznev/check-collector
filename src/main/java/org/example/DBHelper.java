package org.example;

import org.example.pojo.ProductWithTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.example.Config.getConnection;
import static org.example.WorkWithTG.chatId;
import static org.example.WorkWithTG.logger;

public class DBHelper {

    public static void saveOnDB(List<ProductWithTransaction> newProducts) {
        try {
            saveProductWithTransactionToDatabase(newProducts);
            logger.info("Данные успешно сохранены.");
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении данных: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void saveProductWithTransactionToDatabase(List<ProductWithTransaction> products) {
        String maxTransactionNumberQuery = "SELECT COALESCE(MAX(transaction_number), 0) FROM \"product_transactions\"";
        String insertQuery = "INSERT INTO product_transactions (transaction_number, name, price, quantity, total, store_name, transaction_date, amount, chat_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();

             PreparedStatement maxPstmt = conn.prepareStatement(maxTransactionNumberQuery);
             PreparedStatement insertPstmt = conn.prepareStatement(insertQuery)) {

            // Получаем максимальный номер транзакции
            ResultSet rs = maxPstmt.executeQuery();
            int maxTransactionNumber = 0;
            if (rs.next()) {
                maxTransactionNumber = rs.getInt(1);
            }

            // Увеличиваем номер транзакции на 1
            int newTransactionNumber = maxTransactionNumber + 1;

            // Сохраняем продукты с новым номером транзакции
            for (ProductWithTransaction product : products) {
                insertPstmt.setInt(1, newTransactionNumber);
                insertPstmt.setString(2, product.getName());
                insertPstmt.setDouble(3, product.getPrice());
                insertPstmt.setDouble(4, product.getQuantity());
                insertPstmt.setDouble(5, product.getTotal());
                insertPstmt.setString(6, product.getStoreName());
                insertPstmt.setObject(7, product.getDate());
                insertPstmt.setDouble(8, product.getAmount());
                insertPstmt.setLong(9, chatId);
                insertPstmt.executeUpdate();
            }
            logger.info("Продукты успешно сохранены в базе данных!");
        } catch (SQLException e) {
            logger.info("Ошибка при сохранении продуктов: " + e.getMessage());
        }
    }
}
