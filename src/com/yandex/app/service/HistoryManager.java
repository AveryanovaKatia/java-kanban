package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.List;

public interface HistoryManager {
    public void addTaskInHistory(Task task);
    public List<Task> getHistory();
}
