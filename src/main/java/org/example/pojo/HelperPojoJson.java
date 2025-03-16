package org.example.pojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;


public class HelperPojoJson {

    private final ObjectMapper mapper;

    public HelperPojoJson() {
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Игнорируем неизвестные свойства
    }

    public PojoJson.Root parseJson(String jsonString) throws Exception {
        return mapper.readValue(jsonString, PojoJson.Root.class);
    }

}
