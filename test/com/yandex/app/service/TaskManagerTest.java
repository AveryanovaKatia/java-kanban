package com.yandex.app.service;

import com.yandex.app.model.*;
import com.yandex.app.service.exception.IntersectionException;
import com.yandex.app.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task1, task2;
    protected Epic epic1, epic2;
    protected SubTask subTask1, subTask2;

    @BeforeEach
    public void addTest() {
        epic1 = new Epic("Test Epic1", "Test");
        subTask1 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1); //id = 2
        subTask2 = new SubTask("Test SubTask2", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 6, 0), 1); //id = 3
        epic2 = new Epic("Test Epic", "Test");
        task1 = new Task("Test Task1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 0, 0));
        task2 = new Task("Test Task2", "Test", Status.IN_PROGRESS, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0));
    }

    @Test
    void putNewTaskTest() throws NotFoundException {
        manager.putNewTask(task1);
        manager.putNewTask(task2);
        Task t1 = manager.getTaskById(task1.getId());

        assertNotNull(t1, "Задача не добавлена");
        assertEquals(task1, t1, "Это не одна и та же задача");

        Task task3 = new Task("Test Task3", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 0, 0));
        Task task4 = new Task("Test Task4", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 1, 29));
        Task task5 = new Task("Test Task5", "Test", Status.NEW, "PT1H",
                LocalDateTime.of(2024, 1, 1, 0, 10));
        Task task6 = new Task("Test Task6", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2023, 12, 31, 23, 10));

        assertThrows(IntersectionException.class, () -> {manager.putNewTask(task3);},
                "Не работает проверка на пересечение по времени");
        assertThrows(IntersectionException.class, () -> {manager.putNewTask(task4);},
                "Не работает проверка на пересечение по времени");
        assertThrows(IntersectionException.class, () -> {manager.putNewTask(task5);},
                "Не работает проверка на пересечение по времени");
        assertThrows(IntersectionException.class, () -> {manager.putNewTask(task6);},
                "Не работает проверка на пересечение по времени");

        int size = manager.getAllTask().size();

        assertEquals(2, size, "Проверка на интервалы не срабатывает");
    }

    @Test
    void putNewEpicTest() {
        manager.putNewEpic(epic1);
        manager.putNewEpic(epic2);

        Epic e = manager.getEpicById(epic1.getId());
        int size = manager.getAllEpic().size();

        assertNotNull(e, "Эпик равен null");
        assertEquals(epic1, e, "Это не один и тот же эпик");
        assertEquals(2, size, "Эпики добавляются неверно");
    }

    @Test
    void putNewSubTaskTest() {
        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);

        List<Integer> idFromEpic = manager.getEpicById(subTask1.getIdEpic()).getIdSubTasks();
        int i = idFromEpic.get(0); //subTask1

        assertNotNull(subTask1, "Сабтаска равна null");
        assertEquals(subTask2, manager.getSubTaskById(3), "Это не одна и та же сабтаска");
        assertEquals(2, manager.getAllSubTask().size(), "сабтаски добавляются неверно");
        assertEquals(2, i, "При создании сабтаски в эпик неверно записывается ее id");

        SubTask subTask3 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT30M",
                LocalDateTime.of(2024, 1, 1, 4, 10), 1);
        SubTask subTask4 = new SubTask("Test SubTask2", "Test", Status.NEW, "PT2H10M",
                LocalDateTime.of(2024, 1, 1, 5, 50), 1);

        assertThrows(IntersectionException.class, () -> {manager.putNewSubTask(subTask3);},
                "Не работает проверка на пересечение по времени");
        assertThrows(IntersectionException.class, () -> {manager.putNewSubTask(subTask4);},
                "Не работает проверка на пересечение по времени");

        assertEquals(2, manager.getAllSubTask().size(), "Проверка на интервалы не срабатывает");
    }

    //перезаписать задачу
    @Test
    void updateTask() {
        Task task3 = new Task("Test Task2", "Test", Status.DONE, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 0, 0), 1);

        manager.putNewTask(task1);
        manager.updateTask(task3);

        int size = manager.getAllTask().size();

        assertEquals(Status.DONE, manager.getAllTask().get(0).getStatus(), "Изменения не сохранены");
        assertEquals("Test Task2", manager.getAllTask().get(0).getName(), "Изменения не сохранены");
        assertEquals(1, size, "Изменения не сохранены");
    }

    @Test
    void updateEpicTest() {
        Epic epic3 = new Epic("Test Epic3", "Test3", 1);

        manager.putNewEpic(epic1);
        manager.updateEpic(epic3);

        assertEquals("Test Epic3", manager.getEpicById(1).getName(), "Изменения не сохранены");
        assertEquals("Test3", manager.getAllEpic().get(0).getDesc(), "Изменения не сохранены");
    }

    @Test
    void updateSubTaskTest() {
        SubTask subTask3 = new SubTask("Test SubTask3", "Test", Status.DONE, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1, 2);

        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.updateSubTask(subTask3);

        int size = manager.getAllSubTask().size();
        Status st = manager.getAllSubTask().get(0).getStatus();

        assertEquals(Status.DONE, st, "Изменения не сохранены");
        assertEquals(1, size, "Изменения не сохранены");
    }

    //удалить все задачи
    @Test
    void deleteAllTaskTest() {
        manager.putNewTask(task1);
        manager.putNewTask(task2);

        manager.deleteAllTask();

        assertEquals(0, manager.getAllTask().size(), "Задачи не удалены, или удалены не все");
    }

    @Test
    void deleteAllEpicTest() {
        manager.putNewEpic(epic1);
        manager.putNewEpic(epic2);

        manager.deleteAllEpic();

        assertEquals(0, manager.getAllEpic().size(), "Эпики не удалены, или удалены не все");
    }

    @Test
    void deleteAllSubTaskTest() {
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);

        manager.deleteAllSubTask();

        assertEquals(0, manager.getAllSubTask().size(), "Сабтаски не удалены, или удалены не все");
    }

    //получить все задачи
    @Test
    void getAllTaskTest() {
        manager.putNewTask(task1);
        manager.putNewTask(task2);

        List<Task> listTask = manager.getAllTask();

        assertEquals(2, listTask.size(), "Не удалось получить все задачи");
        assertEquals(task1, listTask.get(0), "В полученном списке нет ожидаемой задачи");
        assertEquals(task2, listTask.get(1), "В полученном списке нет ожидаемой задачи");
    }

    @Test
    void getAllEpicTest() {
        manager.putNewEpic(epic1);
        manager.putNewEpic(epic2);

        List<Epic> listEpic = manager.getAllEpic();

        assertEquals(2, listEpic.size(), "Не удалось получить все задачи");
        assertEquals(epic1, listEpic.get(0), "В полученном списке нет ожидаемого эпика");
        assertEquals(epic2, listEpic.get(1), "В полученном списке нет ожидаемого эпика");
    }

    @Test
    void getAllSubTaskTest() {
        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);

        List<SubTask> listSubTask = manager.getAllSubTask();

        assertEquals(2, listSubTask.size(), "Не удалось получить все задачи");
        assertEquals(subTask1, listSubTask.get(0), "В полученном списке нет ожидаемой задачи");
        assertEquals(subTask2, listSubTask.get(1), "В полученном списке нет ожидаемой задачи");
    }

    //получить задачу по id
    @Test
    void getTaskByIdTest() throws NotFoundException {
        manager.putNewTask(task1);
        manager.putNewTask(task2);

        assertEquals(task1, manager.getTaskById(1), "Не удалось получить задачу по id");
        assertEquals(task2, manager.getTaskById(2), "Не удалось получить задачу по id");
    }

    @Test
    void getEpicByIdTest() {
        manager.putNewEpic(epic1);
        manager.putNewEpic(epic2);

        assertEquals(epic1, manager.getEpicById(1), "Не удалось получить эпик по id");
        assertEquals(epic2, manager.getEpicById(2), "Не удалось получить эпик по id");
    }

    @Test
    void getSubTaskByIdTest() {
        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);

        assertEquals(subTask1, manager.getSubTaskById(2), "Не удалось получить сабтаск по id");
        assertEquals(subTask2, manager.getSubTaskById(3), "Не удалось получить сабтаск по id");
    }

    //удалить задачу по id
    @Test
    void deleteTaskByIdTest() {
        manager.putNewTask(task1);
        manager.putNewTask(task2);

        manager.deleteTaskById(1);

        assertEquals(1, manager.getAllTask().size(), "Не удалось удалить задачу по id");
    }

    @Test
    void deleteEpicByIdTest() {
        manager.putNewEpic(epic1);
        manager.putNewEpic(epic2);

        manager.deleteEpicById(1);

        assertEquals(1, manager.getAllEpic().size(), "Не удалось удалить эпик по id");
    }

    @Test
    void deleteSubTaskByIdTest() {
        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);

        manager.deleteSubTaskById(2);

        assertEquals(1, manager.getAllSubTask().size(), "Не удалось удалить сабтаску по id");
    }

    @Test
    void getHistoryTest() throws NotFoundException {
        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);
        manager.putNewTask(task1);
        manager.putNewTask(task2);

        manager.getTaskById(4);
        manager.getEpicById(1);
        manager.getSubTaskById(2);
        manager.getTaskById(5);

        List<Task> list = manager.getHistory();
        assertEquals(4, list.size(), "Не удалось получить истоию запросов задач");
    }

    @Test
    void getPrioritizedTasksTest() {
        manager.putNewEpic(epic1);
        manager.putNewSubTask(subTask1);
        manager.putNewSubTask(subTask2);
        manager.putNewTask(task1);
        manager.putNewTask(task2);

        TreeSet<Task> set = manager.getPrioritizedTasks();

        assertEquals(4, set.size(), "Не удалось добавить задачи в трисет");
        assertTrue(subTask1.getStartTime().isBefore(subTask2.getStartTime()),
                "Задачи отсортированны неверно");
    }
}


