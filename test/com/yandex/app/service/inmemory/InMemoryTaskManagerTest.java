package com.yandex.app.service.inmemory;

import com.yandex.app.model.*;

import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManagerTest;
import com.yandex.app.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createTest() {
        manager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void classManagersAddGoodInMemoryTaskManager() {
        assertNotNull(manager, "Экземпляр класса не пронициализирован");
    }

    // InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerCanAddAnyTaskTest() throws NotFoundException {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        manager.putNewTask(task1); //id = 1

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        manager.putNewEpic(epic1); //id = 2

        SubTask subTask1 = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0), 2); //id = 3
        manager.putNewSubTask(subTask1);

        assertEquals(1, manager.getAllTask().size(), "Неверное количество задач.");
        assertEquals(1, manager.getAllEpic().size(), "Неверное количество задач.");
        assertEquals(1, manager.getAllSubTask().size(), "Неверное количество задач.");

        assertNotNull(manager.getTaskById(1), "Задача не найдена.");
        assertNotNull(manager.getEpicById(2), "Задача не найдена.");
        assertNotNull(manager.getSubTaskById(3), "Задача не найдена.");

        assertEquals(task1, manager.getTaskById(1), "Задачи не совпадают.");
        assertEquals(epic1, manager.getEpicById(2), "Задачи не совпадают.");
        assertEquals(subTask1, manager.getSubTaskById(3), "Задачи не совпадают.");
    }

    // задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void idNotConflictInInMemoryTaskManagerTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        manager.putNewTask(task); //id = 1
        Task task1 = new Task("Test1", "Test1", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.putNewTask(task1); //id = 2
        Task task2 = new Task("Test2", "Test2", Status.IN_PROGRESS,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 4, 0), 2);
        manager.updateTask(task2); //id = 2

        assertEquals(task1, task2, "Задачи не совпадают.");
        assertEquals(2, manager.getAllTask().size(), "Неверное количество задач.");
    }

    // неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void taskShouldBeSOKThenAddInInMemoryTaskManagerTest() throws NotFoundException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        manager.putNewTask(task);
        Task task1 = manager.getTaskById(1);

        assertEquals(task.getName(), task1.getName(), "Несовпадение поля.");
        assertEquals(task.getDesc(), task1.getDesc(), "Несовпадение поля.");
        assertEquals(task.getStatus(), task1.getStatus(), "Несовпадение поля.");
        assertEquals(task.getId(), task1.getId(), "Несовпадение поля.");
    }

    @Test
    public void addEpicStatusTest() {
        Epic epic1 = new Epic("Test Epic", "Test");//id = 1

        SubTask subTask1 = new SubTask("Test subTask1", "Test",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0), 1); //id = 2

        SubTask subTask2 = new SubTask("Test subTask1", "Test",
                Status.IN_PROGRESS, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1); //id = 3

        SubTask subTask3 = new SubTask("Test subTask1", "Test",
                Status.DONE, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1); //id = 4

        manager.putNewEpic(epic1);

        assertEquals(Status.NEW, manager.getAllEpic().get(0).getStatus(), "Статус эпика расчитан неверно.");

        manager.putNewSubTask(subTask1);

        assertEquals(Status.NEW, manager.getAllEpic().get(0).getStatus(), "Статус эпика расчитан неверно.");

        manager.putNewSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, manager.getAllEpic().get(0).getStatus(),
                "Статус эпика расчитан неверно.");

//        manager.putNewSubTask(subTask3);
//        так же пересечение

        assertEquals(Status.IN_PROGRESS, manager.getAllEpic().get(0).getStatus(),
                "Статус эпика расчитан неверно.");

        manager.deleteAllSubTask();
        manager.putNewSubTask(subTask3);

        assertEquals(Status.DONE, manager.getAllEpic().get(0).getStatus(), "Статус эпика расчитан неверно.");
    }
}
