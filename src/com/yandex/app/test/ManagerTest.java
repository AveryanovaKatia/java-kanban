package com.yandex.app.test;

import com.yandex.app.model.*;
import com.yandex.app.service.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagerTest {
    Managers managers;

    @BeforeEach
    public void addTaskTest() {
        managers = new Managers();
    }

    // убедитесь, что утилитарный класс всегда возвращает проинициализ-ые и готовые к работе экземпляры менеджеров;
    @Test
    public void classManagersAddGoodInMemoryTaskManager() {
        TaskManager taskManager = managers.getDefault();
        assertNotNull(taskManager);
    }


    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerCanAddAnyTaskTest() {
        TaskManager taskManager = managers.getDefault();
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.putNewTask(task1); //id = 1

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.putNewEpic(epic1); //id = 2

        SubTask subTask1 = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                Status.NEW, 2); //id = 3
        taskManager.putNewSubTask(subTask1);

        assertEquals(1, taskManager.getAllTask().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getAllEpic().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getAllSubTask().size(), "Неверное количество задач.");

        assertNotNull(taskManager.getTaskById(1), "Задача не найдена.");
        assertNotNull(taskManager.getEpicById(2), "Задача не найдена.");
        assertNotNull(taskManager.getSubTaskById(3), "Задача не найдена.");

        assertEquals(task1, taskManager.getTaskById(1), "Задачи не совпадают.");
        assertEquals(epic1, taskManager.getEpicById(2), "Задачи не совпадают.");
        assertEquals(subTask1, taskManager.getSubTaskById(3), "Задачи не совпадают.");
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void idNotConflictInInMemoryTaskManagerTest() {
        TaskManager taskManager = managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.putNewTask(task); //id = 1
        Task task1 = new Task("Test1", "Test1", Status.NEW);
        taskManager.putNewTask(task1); //id = 2
        Task task2 = new Task("Test2", "Test2", Status.IN_PROGRESS, 2);
        taskManager.updateTask(task2); //id = 2

        assertEquals(task1, task2, "Задачи не совпадают.");
        assertEquals(2, taskManager.getAllTask().size(), "Неверное количество задач.");
    }

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void taskShouldBeSOKThenAddInInMemoryTaskManagerTest() {
        TaskManager taskManager = managers.getDefault();
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.putNewTask(task);
        Task task1 = taskManager.getTaskById(1);

        assertEquals(task.getName(), task1.getName(), "Несовпадение поля.");
        assertEquals(task.getDesc(), task1.getDesc(), "Несовпадение поля.");
        assertEquals(task.getStatus(), task1.getStatus(), "Несовпадение поля.");
        assertEquals(task.getId(), task1.getId(), "Несовпадение поля.");
    }

    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void addTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        historyManager.addTaskInHistory(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}
