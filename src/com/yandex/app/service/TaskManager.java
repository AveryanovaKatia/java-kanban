package com.yandex.app.service;

import com.yandex.app.model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();


    // добавть новую задачу
    public void putNewTask(Task task) {
        int id = makeId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void putNewEpic(Epic epic) {
        int id = makeId();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void putNewSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getIdEpic())) {
            int id = makeId();
            subTask.setId(id);
            subTasks.put(id, subTask);
            epics.get(subTask.getIdEpic()).addSubTask(id);
            ArrayList<Status> allStatusEpic = getSubTaskStatus(epics.get(subTask.getIdEpic()).getOllSubTask());
            epics.get(subTask.getIdEpic()).makeStatusList(allStatusEpic);
        }
    }

    //перезаписать задачу
    public void updateTask(Task task) {
        if (tasks.containsValue(task)) {
            // по-моему это теперь не работает(
            Task oldTask = tasks.get(task.getId());
            int id = oldTask.getId();
            tasks.put(id, task);
            task.setId(id);
        }
    }

    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setName(epic.getName());
            oldEpic.setDesc(epic.getDesc());
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsValue(subTask)) {
            // по-моему это теперь не работает(
            SubTask oldSubTask = subTasks.get(subTask.getId());
            int id = oldSubTask.getId();
            int idEpic = oldSubTask.getIdEpic();
            subTask.setId(id);
            subTasks.put(id, subTask);
            epics.get(idEpic).deleteSubTask(id);
            epics.get(idEpic).addSubTask(id);
            ArrayList<Status> allStatusEpic = getSubTaskStatus(epics.get(idEpic).getOllSubTask());
            epics.get(idEpic).makeStatusList(allStatusEpic);
        }
    }

    //удалить все задачи
    public void deleteOllTask() {
        tasks.clear();
    }

    public void deleteOllEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteOllSubTask() {
        for (int id : epics.keySet()) {
            epics.get(id).clearSubtasks();
        }
        subTasks.clear();
    }

    //получить все задачи
    public ArrayList<Task> getOllTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getOllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getOllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    //получить задачу по id
    public String getTaskById(int id) {
        return tasks.get(id).toString();
    }

    public String getEpicById(int id) {
        return epics.get(id).toString();
    }

    public String getSubTaskById(int id) {
        return subTasks.get(id).toString();
    }

    //удалить задачу по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        for (int i : epics.get(id).getOllSubTask()) {
            subTasks.remove(i);
        }
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        int idEpic = subTask.getIdEpic();
        epics.get(idEpic).deleteSubTask(id);
        ArrayList<Status> allStatusEpic = getSubTaskStatus(epics.get(idEpic).getOllSubTask());
        epics.get(idEpic).makeStatusList(allStatusEpic);

    }


    public ArrayList<Status> getSubTaskStatus(ArrayList<Integer> i) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (int a : i) {
            statuses.add(subTasks.get(a).getStatus());
        }
        return statuses;
    }

    private int makeId() {
        return id++;
    }
}





