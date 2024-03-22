package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList= new LinkedList<>();
    private final static int SIZE = 10;

    @Override
    public void addTaskInHistory(Task task) {
        if (task != null) {
            if (historyList.size() == SIZE) {
                historyList.remove(0);
            }
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyList);
    }







}
