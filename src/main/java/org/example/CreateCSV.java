package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.Config.getConnection;
import static org.example.WorkWithTG.logger;
public class CreateCSV {

    public static String getAllProductsAsCsv() {
        StringBuilder csvBuilder = new StringBuilder();
        String selectQuery = "SELECT id, transaction_number, name, price, quantity, total, store_name, transaction_date, amount FROM product_transactions";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            // Добавляем заголовки CSV
            csvBuilder.append("id,Transaction Number,Name,Price,Quantity,Total,Store Name,Transaction Date,Amount\n");

            // Обрабатываем результаты запроса
            while (rs.next()) {
                csvBuilder.append(rs.getInt("id")).append(",")
                        .append(rs.getInt("transaction_number")).append(",")
                        .append(escapeCsvValue(rs.getString("name"))).append(",")
                        .append(escapeCsvValue(rs.getDouble("price"))).append(",")
                        .append(rs.getFloat("quantity")).append(",")
                        .append(escapeCsvValue(rs.getDouble("total"))).append(",")
                        .append(escapeCsvValue(rs.getString("store_name"))).append(",")
                        .append(rs.getObject("transaction_date")).append(",")
                        .append(escapeCsvValue(rs.getDouble("amount")))
                        .append("\n");
            }

            logger.info("Данные успешно извлечены и преобразованы в CSV формат!");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при извлечении данных: " + e.getMessage());
        }

        return csvBuilder.toString();
    }

    // Метод для экранирования значений CSV
    private static String escapeCsvValue(Object value) {
        if (value == null) {
            return "\"\""; // Возвращаем пустую строку в кавычках для null значений
        }
        String stringValue = value.toString();
        // Экранируем кавычки
        stringValue = stringValue.replace("\"", "\"\"");
        // Если значение содержит запятую или кавычку, заключаем его в кавычки
        if (stringValue.contains(".")) {
            stringValue = stringValue.replace(".", ",");
        }
        if (stringValue.contains(",") || stringValue.contains("\"")) {
            stringValue = "\"" + stringValue + "\"";
        }

        return stringValue;
    }



}
