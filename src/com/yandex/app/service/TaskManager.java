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
            epic.addIdSubTasks(id);
            updateStatusEpic(epic);
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
            updateStatusEpic(epic);
        }
    }

    //удалить все задачи
    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTask() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateStatusEpic(epic);
        }
        subTasks.clear();
    }

    //получить все задачи
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTask() {
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
        updateStatusEpic(epic);
    }

    private void updateStatusEpic(Epic epic) {
        int statusIsNew = 0;
        int statusIsInProgress = 0;
        int statusIsDone = 0;
        if (epic.getIdSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            for (int idS : epic.getIdSubTasks()) {
                if (subTasks.get(idS).getStatus().equals(Status.NEW)) {
                    statusIsNew++;
                } else if (subTasks.get(idS).getStatus().equals(Status.IN_PROGRESS)) {
                    statusIsInProgress++;
                } else {
                    statusIsDone++;
                }

                if (statusIsInProgress == 0 && statusIsDone == 0) {
                    epic.setStatus(Status.NEW);
                } else if (statusIsNew == 0 && statusIsInProgress == 0) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    private int makeId() {
        return id++;
    }
}





