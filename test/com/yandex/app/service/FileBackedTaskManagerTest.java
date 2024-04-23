package com.yandex.app.service;

import com.yandex.app.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private static File file;
    private static FileBackedTaskManager fbtm;

    public FileBackedTaskManagerTest() {
    }

    @BeforeEach
    public void addTaskTest() throws IOException {
        file = File.createTempFile("test", "txt");
        fbtm = new FileBackedTaskManager(file);
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
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        fbtm.putNewTask(task1); //id = 1

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        fbtm.putNewEpic(epic1); //id = 2

        SubTask subTask1 = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                Status.NEW, 2); //id = 3
        fbtm.putNewSubTask(subTask1);

        String fr = Files.readString(file.toPath());
        String[] lines = fr.split(";");


        assertEquals(4, lines.length, "Количество строк не совпадает с ожидаемым");
        assertEquals("id,type,name,description,status,epic", lines[0],
                "Базовая строка не добавлена");
        assertEquals("1,TASK,Test addNewTask,Test addNewTask description,NEW", lines[1],
                "Задачи добавляются неверно");

    }

    @Test
    public void writeAndDeleteTasksInFileTest() throws IOException {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        fbtm.putNewTask(task1); //id = 1

        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        fbtm.putNewTask(task2); //id = 2
        fbtm.deleteTaskById(2);

        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        fbtm.putNewEpic(epic1); //id = 3
        fbtm.deleteAllEpic();

        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, f.tasks.size(), "Задачи удаляютя неверно");
        assertEquals(0, f.epics.size(), "Задачи удаляютя неверно");

    }

    @Test
    public void fillingIDSubTaskInEpicTest() throws IOException {
        Epic epic1 = new Epic("Test addNewEpic", "Test addNewEpic description");
        fbtm.putNewEpic(epic1); //id = 1

        SubTask subTask1 = new SubTask("Test addNewSubTask1", "Test",
                Status.NEW, 1); //id = 2
        fbtm.putNewSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test addNewSubTask2", "Test",
                Status.NEW, 1); //id = 3
        fbtm.putNewSubTask(subTask2);

        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);
        Epic epic = f.getEpicById(1);
        List<Integer> list = epic.getIdSubTasks();

        assertEquals(2, list.size(), "Для эпика неверно заполняется лист айди с сабтасков");
    }
}
