package com.hades.example.java.lib;

import com.google.gson.Gson;

public class JsonUtil {
    private final Gson gson = new Gson();

    public <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception ex) {
            return null;
        }
    }
}
