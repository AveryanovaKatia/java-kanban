package com.yandex.app.service;

import com.yandex.app.service.inmemory.InMemoryHistoryManager;
import com.yandex.app.service.inmemory.InMemoryTaskManager;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
