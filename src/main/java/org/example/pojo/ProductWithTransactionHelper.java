package org.example.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.ConvertValue.formatNameOfProduct;
import static org.example.ConvertValue.replaceStoreName;

public class ProductWithTransactionHelper {

    public static List<ProductWithTransaction> convertRootsToProductWithTransactions(PojoJson.Root root) {
        DateTimeFormatter dateTimeFormatToCheckTime =  DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        List<ProductWithTransaction> productsWithTransaction = new ArrayList<>();

        // Обработка продуктов
        for (PojoJson.Item item : root.data.json.items) {
            ProductWithTransaction product = new ProductWithTransaction(
                    formatNameOfProduct(item.name),
                    item.price / 100,
                    item.quantity,
                    "",
                    LocalDateTime.now()
            );
            productsWithTransaction.add(product);
        }

        // Обработка транзакции
        String retailPlace = root.data.json.user + " - " + root.data.json.retailPlace;
        retailPlace = replaceStoreName(retailPlace);

        String ecashTotalSum = (root.data.json.ecashTotalSum / 100) + "." + (root.data.json.ecashTotalSum % 100);

        // Извлекаем значение check_time
        String checkTimeString = root.request.manual.check_time;
        checkTimeString = checkTimeString.replace('t', 'T');
        LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, dateTimeFormatToCheckTime);

        // Обновляем все продукты с данными о транзакции
        for (ProductWithTransaction product : productsWithTransaction) {
            product.setStoreName(retailPlace);
            product.setDate(checkTime);
            product.setAmount(Double.parseDouble(ecashTotalSum));
        }

        return productsWithTransaction;
    }

}