package com.yandex.app.model;

import com.yandex.app.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubTaskTest {
    private TaskManager taskManager;

    @BeforeEach
    public void addTaskTest() {
        taskManager = Managers.getDefault();
    }

    //  проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void extendsTasksEqualsIfEqualsTheirIdTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.putNewEpic(epic);

        SubTask subTask1 = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                Status.NEW, 1);
        taskManager.putNewSubTask(subTask1);

        final int id = subTask1.getId();

        SubTask subTask2 = new SubTask("Test3", "Test4", Status.IN_PROGRESS, 1, id);
        taskManager.updateSubTask(subTask2);

        assertEquals(subTask1, subTask2, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTask();

        assertNotNull(subTask1, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask2, subTasks.get(0), "Задачи не совпадают.");
    }
}
