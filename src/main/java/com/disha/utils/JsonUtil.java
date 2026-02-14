package com.disha.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
    .enable(SerializationFeature.INDENT_OUTPUT);

    public static String prettyPrint(String json) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            return json;
        }
    }
}