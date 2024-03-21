package com.yandex.app.service;

import com.yandex.app.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    // добавть новую задачу
    @Override
    public void putNewTask(Task task) {
        int id = makeId();
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void putNewEpic(Epic epic) {
        int id = makeId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
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
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setName(epic.getName());
            oldEpic.setDesc(epic.getDesc());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            final Epic epic = epics.get(subTask.getIdEpic());
            updateStatusEpic(epic);
        }
    }

    //удалить все задачи
    @Override
    public void deleteAllTask() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTask() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateStatusEpic(epic);
        }
        subTasks.clear();
    }

    //получить все задачи
    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    //получить задачу по id
    @Override
    public Task getTaskById(int id) {
        historyManager.addTaskInHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addTaskInHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.addTaskInHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    //удалить задачу по id
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (int i : epics.get(id).getIdSubTasks()) {
            subTasks.remove(i);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        final Epic epic = epics.get(subTask.getIdEpic());
        epic.deleteSubTask(id);
        updateStatusEpic(epic);
    }

    @Override
    public HistoryManager getHistory() {
        return historyManager;
    }


    private void updateStatusEpic(Epic epic) {
        int statusIsNew = 0;
        int statusIsDone = 0;
        for (int idS : epic.getIdSubTasks()) {
            Status status = subTasks.get(idS).getStatus();
            if (status.equals(Status.NEW)) {
                statusIsNew++;
            } else if (status.equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else {
                statusIsDone++;
            }
        }
        if (statusIsNew == epic.getIdSubTasks().size()) {
            epic.setStatus(Status.NEW);
        } else if (statusIsDone == epic.getIdSubTasks().size() && statusIsDone != 0) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private int makeId() {
        return id++;
    }
}





