package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyManager = new LinkedList<>();
    private final static int size = 10;

    @Override
    public void addTaskInHistory(Task task) {
      if (historyManager.size() == size) {
          historyManager.remove(0);
      }
      historyManager.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyManager);
    }







}
