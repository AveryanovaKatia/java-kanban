package com.yandex.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.service.inmemory.InMemoryHistoryManager;
import com.yandex.app.service.inmemory.InMemoryTaskManager;

import java.time.LocalDateTime;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

//    public static Gson getGson() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
//        return gsonBuilder.create();
//    }
}
