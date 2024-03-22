package com.yandex.app.model;

import com.yandex.app.service.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskTest {
    private TaskManager taskManager;

    @BeforeEach
    public void addTaskTest() {
        taskManager = Managers.getDefault();
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void twoTasksEqualsIfEqualsTheirIdTest() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.putNewTask(task1);

        final int id = task1.getId();

        Task task2 = new Task("Test1", "Test2", Status.IN_PROGRESS, id);
        taskManager.updateTask(task2);

        assertNotNull(task2, "Задача не найдена.");
        assertEquals(task1, task2, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }
}
