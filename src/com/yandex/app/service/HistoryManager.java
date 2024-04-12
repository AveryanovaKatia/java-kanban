package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.List;

public interface HistoryManager {
    void addTaskInHistory(Task task);

    List<Task> getHistory();

    void removeTaskFromHistory(int id);
}
