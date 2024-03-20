package com.yandex.app.test;

import com.yandex.app.model.*;
import com.yandex.app.service.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskTest {
    Managers managers;
    TaskManager taskManager;

    @BeforeEach
    public void addTaskTest() {
        managers = new Managers();
        taskManager = managers.getDefault();
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

        final ArrayList<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
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

        final ArrayList<SubTask> subTasks = taskManager.getAllSubTask();

        assertNotNull(subTask1, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask2, subTasks.get(0), "Задачи не совпадают.");
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи; - НЕВОЗМОЖНО (разные параметры)
    // проверьте, что объект Subtask нельзя сделать своим же эпиком; - НЕВОЗМОЖНО (разные параметры)
}
