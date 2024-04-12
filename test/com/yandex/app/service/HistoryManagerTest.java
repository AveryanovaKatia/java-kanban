package com.yandex.app.service;

import com.yandex.app.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void addTaskTest() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void addTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, 1);
        Task task2 = new Task("Test add 2", "Test addNewTask description", Status.NEW, 2);
        historyManager.addTaskInHistory(task);
        historyManager.addTaskInHistory(task2);

        final List<Task> history = historyManager.getHistorys();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
    }

    @Test
    public void remuveRepitTest() {
        Task task1 = new Task("Test add 1", "Test addNewTask description", Status.NEW, 1);
        Task task2 = new Task("Test add 2", "Test addNewTask description", Status.NEW, 2);
        Task task3 = new Task("Test add 3", "Test addNewTask description", Status.NEW, 3);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task3);

        final List<Task> history = historyManager.getHistorys();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "В истории нет повторения.");
    }

    @Test
    public void taskInOrderOfAdditionTest() {
        Task task1 = new Task("Test add 1", "Test addNewTask description", Status.NEW, 1);
        Task task2 = new Task("Test add 2", "Test addNewTask description", Status.NEW, 2);
        Task task3 = new Task("Test add 3", "Test addNewTask description", Status.NEW, 3);
        Task task4 = new Task("Test add 4", "Test addNewTask description", Status.NEW, 4);

        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task4);

        final List<Task> history = historyManager.getHistorys();

        assertEquals(task1, history.get(0), "Задачи хранятся в порядке добавления.");
        assertEquals(task2, history.get(1), "Задачи хранятся в порядке добавления.");
        assertEquals(task3, history.get(2), "Задачи хранятся в порядке добавления.");
        assertEquals(task4, history.get(3), "Задачи хранятся в порядке добавления.");
    }
}
