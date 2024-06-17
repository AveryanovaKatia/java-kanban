package com.yandex.app.service;

import com.yandex.app.model.*;
import com.yandex.app.service.exception.NotFoundException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm");
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // добавть новую задачу
    void putNewTask(Task task);

    void putNewEpic(Epic epic);

    void putNewSubTask(SubTask subTask);

    //перезаписать задачу
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    //удалить все задачи
    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubTask();

    //получить все задачи
    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<SubTask> getAllSubTask();

    //получить задачу по id
    Task getTaskById(int id) throws NotFoundException;

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    //удалить задачу по id
    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}

