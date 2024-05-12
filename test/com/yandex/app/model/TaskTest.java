package com.yandex.app.model;

import com.yandex.app.service.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskTest {
    private TaskManager taskManager;

    @BeforeEach
    public void createTest() {
        taskManager = Managers.getDefault();
    }

    // экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void twoTasksEqualsIfEqualsTheirIdTest() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.putNewTask(task1);

        final int id = task1.getId();

        Task task2 = new Task("Test1", "Test2", Status.IN_PROGRESS,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0), id);
        taskManager.updateTask(task2);

        assertNotNull(task2, "Задача не найдена.");
        assertEquals(task1, task2, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void setFotTaskTest() {
        Task task1 = new Task("Test1", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.putNewTask(task1);
        task1.setName("TestNoTest");
        task1.setDesc("DescNoDesc");
        task1.setStatus(Status.DONE);
        task1.setId(123);

        assertEquals("TestNoTest", task1.getName(), "Изменения не сохранены");
        assertEquals("DescNoDesc", task1.getDesc(), "Изменения не сохранены");
        assertEquals(Status.DONE, task1.getStatus(), "Изменения не сохранены");
        assertEquals(123, task1.getId(), "Изменения не сохранены");
    }
}
