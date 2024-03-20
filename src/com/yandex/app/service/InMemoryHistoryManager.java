package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> historyManager = new LinkedList<>();

    @Override
    public void addTaskInHistory(Task task) {
      if (historyManager.size() < 10) {
          historyManager.add(task);
      } else {
          historyManager.remove(0);
          historyManager.add(task);
      }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager;
    }







}
