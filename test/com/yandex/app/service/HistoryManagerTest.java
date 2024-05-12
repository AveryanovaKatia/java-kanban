package com.yandex.app.service;

import com.yandex.app.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void createTest() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void addTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        Task task2 = new Task("Test add 2", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        historyManager.addTaskInHistory(task);
        historyManager.addTaskInHistory(task2);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(2, history.size(), "История пустая.");
    }

    @Test
    public void removingRepetitionsTest() {
        Task task1 = new Task("Test add 1", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        Task task2 = new Task("Test add 2", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        Task task3 = new Task("Test add 3", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 4, 0), 3);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task3);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Независимо от добавляемых задач, история пуста.");
        assertEquals(3, history.size(), "В истории повторения.");
    }

    @Test
    public void taskInOrderOfAdditionTest() {
        Task task1 = new Task("Test add 1", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        Task task2 = new Task("Test add 2", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0), 2);
        Task task3 = new Task("Test add 3", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 4, 0), 3);
        Task task4 = new Task("Test add 4", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 6, 0), 4);

        assertEquals(0, historyManager.getHistory().size(),
                "Вне зависимотри от ожиданий история не пуста.");

        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task4);

        final List<Task> history = historyManager.getHistory();

        assertEquals(task1, history.get(0), "Хранение в порядке добавления задач нарушено.");
        assertEquals(task2, history.get(1), "Хранение в порядке добавления задач нарушено.");
        assertEquals(task3, history.get(2), "Хранение в порядке добавления задач нарушено.");
        assertEquals(task4, history.get(3), "Хранение в порядке добавления задач нарушено.");

        historyManager.removeTaskFromHistory(1);

        assertEquals(3, historyManager.getHistory().size(),
                "Задачи не удаляются из истории.");
    }
}
