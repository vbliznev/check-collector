package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelperPojoJson {

    private final ObjectMapper mapper;

    public HelperPojoJson() {
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Игнорируем неизвестные свойства
    }

    public PojoJson.Root parseJson(String jsonString) throws Exception {
        return mapper.readValue(jsonString, PojoJson.Root.class);
    }

    public static String convertToPojo(PojoJson.Root root) {

        if (root == null || root.data.json == null) {
            return "";
        }
        PojoJson.Data data = root.data;

        StringBuilder result = new StringBuilder();
        // Извлекаем значение check_time
        String checkTimeString = root.request.manual.check_time;
        // Приводим строку к правильному формату
        checkTimeString = checkTimeString.replace('t', 'T');
        // Парсим строку в LocalDateTime
        LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        // Форматируем дату и время
        String formattedTime = checkTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String formattedDate = checkTime.format(DateTimeFormatter.ofPattern("d MMMM"));

        // Добавляем информацию о дате, времени, месте и общей сумме
        result.append(formattedDate)
                .append(" - ")
                .append(data.json.user)
                .append(" - ")
                .append(formattedTime)
                .append("\n")
                .append("Сумма: ")
                .append("\n")
                .append(data.json.ecashTotalSum / 100)
                .append(".")
                .append(data.json.ecashTotalSum % 100)
                .append("\n");

        // Проходимся по списку товаров
        for (int i = 0; i < data.json.items.size(); i++) {
            PojoJson.Item item = data.json.items.get(i);

            result.append(i + 1)
                    .append(") ")
                    .append(item.name)
                    .append(" - ")
                    .append(item.price / 100)
                    .append(".")
                    .append(item.price % 100)
                    .append(" - ")
                    .append(item.quantity)
                    .append("\n");
        }
        return result.toString().trim();
    }

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void saveJsonToFile(String jsonResponse, String folderPath, String fileName) throws JsonProcessingException {
        ObjectNode json = (ObjectNode) objectMapper.readTree(jsonResponse);

        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Путь к файлу, куда будем сохранять JSON
        String filePath = folderPath + "/" + fileName + ".json";

        // Сохраняем JSON в файл
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), json);
            System.out.println("JSON успешно сохранен в " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
