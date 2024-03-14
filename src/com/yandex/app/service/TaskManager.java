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
        Epic epic = epics.get(subTask.getIdEpic());
        if (epic != null) {
            int id = makeId();
            subTask.setId(id);
            subTasks.put(id, subTask);
            epic.setIdSubTasks(id);
            epic.setStatus(getSubTaskStatus(epic));
        }
    }

    //перезаписать задачу
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
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
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            final Epic epic = epics.get(subTask.getIdEpic());
            epic.setIdSubTasks(subTask.getId());
            epic.setStatus(getSubTaskStatus(epic));
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
        Epic epic;
        for (int id : epics.keySet()) {
            epic = epics.get(id);
            epic.clearSubtasks();
            epic.setStatus(getSubTaskStatus(epic));
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
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    //удалить задачу по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        for (int i : epics.get(id).getIdSubTasks()) {
            subTasks.remove(i);
        }
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        final Epic epic = epics.get(subTask.getIdEpic());
        epic.deleteSubTask(id);
        epic.setStatus(getSubTaskStatus(epic));
    }

    public Status getSubTaskStatus(Epic epic) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (int idS : epic.getIdSubTasks()) {
            statuses.add(subTasks.get(idS).getStatus());
        }
        if (statuses.isEmpty()) {
            return Status.NEW;
        } else {
            if (!statuses.contains(Status.IN_PROGRESS) && !statuses.contains(Status.DONE)) {
                return Status.NEW;
            } else if (!statuses.contains(Status.NEW) && !statuses.contains(Status.IN_PROGRESS)) {
                return Status.DONE;
            } else {
                return Status.IN_PROGRESS;
            }
        }
    }

    private int makeId() {
        return id++;
    }
}





