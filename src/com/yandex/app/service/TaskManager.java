package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
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
    ArrayList<Task> getAllTask();

    ArrayList<Epic> getAllEpic();

    ArrayList<SubTask> getAllSubTask();

    //получить задачу по id
    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    //удалить задачу по id
    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    public HistoryManager getHistory();
}
