import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pojo.PojoJson;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonProcessor {

    public static final String SUCCESSFUL_PATH = "src/main/java/org/example/successfulJson";
    public static final String OUTPUT_PATH = "src/main/java/org/example/successfulJson/new";

    public static void main(String[] args) {
        File folder = new File(SUCCESSFUL_PATH);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    processJsonFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void processJsonFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PojoJson.Root root = objectMapper.readValue(file, PojoJson.Root.class);

        // Извлекаем значение check_time
        String checkTimeString = root.request.manual.check_time;

        // Приводим строку к правильному формату
        checkTimeString = checkTimeString.replace('t', 'T');

        // Парсим строку в LocalDateTime
        LocalDateTime checkTime = LocalDateTime.parse(checkTimeString, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));

        // Форматируем имя файла
        String formattedFileName = formatFileName(checkTime, root.request.manual.sum);

        // Сохраняем измененный JSON в новую папку
        objectMapper.writeValue(new File(OUTPUT_PATH + "/" + formattedFileName + ".json"), root);
    }

    private static String formatFileName(LocalDateTime checkTime, String sum) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yy__HH_mm");
        return checkTime.format(formatter) + "__" + sum;
    }
}