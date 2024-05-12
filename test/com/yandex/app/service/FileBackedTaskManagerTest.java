package com.yandex.app.service;

import com.yandex.app.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    private static File file;

    @BeforeEach
    public void createTest() throws IOException {
        file = File.createTempFile("test", "txt");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void readTasksInEmptyFileTest() throws IOException {
        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);
        List<Task> list = f.getAllTask();
        List<Epic> list1 = f.getAllEpic();
        List<SubTask> list2 = f.getAllSubTask();

        assertEquals(0, list.size(),
                "Из пустого файла зкземпляр класса FileBackedTaskManager создается не пустым.");
        assertEquals(0, list1.size(),
                "Из пустого файла зкземпляр класса FileBackedTaskManager создается не пустым");
        assertEquals(0, list2.size(),
                "Из пустого файла зкземпляр класса FileBackedTaskManager создается не пустым");
    }

    @Test
    public void writeTasksInFileTest() throws IOException {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        manager.putNewTask(task1); //id = 1

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        manager.putNewEpic(epic1); //id = 2

        SubTask subTask1 = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0), 2); //id = 3
        manager.putNewSubTask(subTask1);

        String fr = Files.readString(file.toPath());
        String[] lines = fr.split(";");


        assertEquals(4, lines.length, "Количество строк не совпадает с ожидаемым");
        assertEquals("id,type,name,description,status,startTime,duration,epic", lines[0],
                "Базовая строка не добавлена");
        assertEquals("1,TASK,Test addNewTask,Test addNewTask description,NEW,PT1H30M,01.01.2024_00:00",
                lines[1], "Задачи добавляются неверно");
    }

    @Test
    public void writeAndDeleteTasksInFileTest() throws IOException {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        manager.putNewTask(task1); //id = 1

        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0));
        manager.putNewTask(task2); //id = 2
        manager.deleteTaskById(2);

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        manager.putNewEpic(epic1); //id = 3
        manager.deleteAllEpic();

        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, f.tasks.size(), "Задачи удаляютя неверно");
        assertEquals(0, f.epics.size(), "Задачи удаляютя неверно");

    }

    @Test
    public void fillingIDSubTaskInEpicTest() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        manager.putNewEpic(epic1); //id = 1

        SubTask subTask1 = new SubTask("Test addNewSubTask1", "Test", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        manager.putNewSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test addNewSubTask2", "Test", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        manager.putNewSubTask(subTask2);

        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);
        Epic epic = f.getEpicById(1);
        List<Integer> list = epic.getIdSubTasks();

        assertEquals(2, list.size(), "Для эпика неверно заполняется лист айди с сабтасков");
        assertEquals(3, list.get(1), "Для эпика неверно заполняется лист айди с сабтасков");
    }

    @Test
    public void writeAndReadFileTest() throws IOException {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        manager.putNewTask(task1); //id = 1

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        manager.putNewEpic(epic1); //id = 2

        SubTask subTask1 = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0), 2); //id = 3
        manager.putNewSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test addNewSubTask2", "Test",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 5, 0), 2); //id = 4
        manager.putNewSubTask(subTask2);

        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm");

        Epic epicTest = f.getEpicById(2);
        String startTimeEpicTest = epicTest.getStartTime().format(formatter);
        String endTimeEpicTest = epicTest.getEndTimeEpic().format(formatter);
        String durationEpicTest = epicTest.getDuration().toString();

        assertEquals("01.01.2024_02:00", startTimeEpicTest,
                "Время начала выполнения эпика записано в файл и после воссоздано неверно");
        assertEquals("01.01.2024_06:30", endTimeEpicTest,
                "Время завершения выполнения эпика расчитано неверно");
        assertEquals("PT4H30M", durationEpicTest,
                "Время выполнения эпика переданно неверно");
    }
}
