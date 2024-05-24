package com.yandex.app.service.inmemory;

import com.yandex.app.model.*;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.exception.IntersectionException;
import com.yandex.app.service.exception.NotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected int idCount = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    // добавть новую задачу
    @Override
    public void putNewTask(Task task) {
        if (!intersectionTaskTime(task)) {
            int id = makeId();
            task.setId(id);
            tasks.put(id, task);
        } else {
            throw new IntersectionException("Задача пересекается по времени с уже добавленными");
        }
    }

    @Override
    public void putNewEpic(Epic epic) {
        int id = makeId();
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void putNewSubTask(SubTask subTask) {
        if (!intersectionTaskTime(subTask)) {
            Epic epic = epics.get(subTask.getIdEpic());
            if (epic != null) {
                int id = makeId();
                subTask.setId(id);
                subTasks.put(id, subTask);
                epic.addIdSubTasks(id);
                updateEpicFields(epic);
            }
        } else {
            throw new IntersectionException("Задача пересекается по времени с уже добавленными");
        }
    }

    //перезаписать задачу
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            Task oldTask = tasks.remove(id);
            if (!intersectionTaskTime(task)) {
                tasks.put(id, task);
            } else {
                tasks.put(id, oldTask);
                throw new IntersectionException("Задача пересекается по времени с уже добавленными");
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (Objects.nonNull(oldEpic)) {
            oldEpic.setName(epic.getName());
            oldEpic.setDesc(epic.getDesc());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int id = subTask.getId();
        final Epic epic = epics.get(subTask.getIdEpic());
        if (subTasks.containsKey(id)) {
            SubTask oldSubTask = subTasks.remove(id);
            epic.deleteSubTask(id);
            updateEpicFields(epic);
            if (!intersectionTaskTime(subTask)) {
                subTasks.put(id, subTask);
                updateEpicFields(epic);
            } else {
                subTasks.put(id, oldSubTask);
                updateEpicFields(epic);
                throw new IntersectionException("Задача пересекается по времени с уже добавленными");
            }
        }
    }

    //удалить все задачи
    @Override
    public void deleteAllTask() {
        tasks.keySet().forEach(historyManager::removeTaskFromHistory);
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        epics.keySet().forEach(historyManager::removeTaskFromHistory);
        subTasks.keySet().forEach(historyManager::removeTaskFromHistory);
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTask() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateStatusEpic(epic);
        }
        subTasks.keySet().forEach(historyManager::removeTaskFromHistory);
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
    public Task getTaskById(int id) throws NotFoundException {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.addTaskInHistory(task);
            return task;
        }
        throw new NotFoundException(id + " не соответсвует ни одной из созданных задач");
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            Task task = epics.get(id);
            historyManager.addTaskInHistory(task);
            return epics.get(id);
        }
        throw new NotFoundException(id + "не соответсвует ни одному из созданных Эпиков");
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
        Task task = subTasks.get(id);
        historyManager.addTaskInHistory(task);
        return subTasks.get(id);
    }
        throw new NotFoundException(id + " не соответсвует ни одной из созданных задач");
    }

    //удалить задачу по id
    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
        historyManager.removeTaskFromHistory(id);
        tasks.remove(id);
        } else {
            throw new NotFoundException(id + " не соответсвует ни одной из созданных задач");
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
        for (int i : epics.get(id).getIdSubTasks()) {
            historyManager.removeTaskFromHistory(i);
            subTasks.remove(i);
        }
        historyManager.removeTaskFromHistory(id);
        epics.remove(id);
        } else {
            throw new NotFoundException(id + "не соответсвует ни одному из созданных Эпиков");
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
        SubTask subTask = subTasks.remove(id);
        final Epic epic = epics.get(subTask.getIdEpic());
        historyManager.removeTaskFromHistory(id);
        epic.deleteSubTask(id);
        updateEpicFields(epic);
        } else {
            throw new NotFoundException(id + " не соответсвует ни одной из созданных задач");
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        final TreeSet<Task> setTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));

        tasks.values().stream().filter(task -> task.getStartTime() != null).forEach(setTask::add);
        subTasks.values().stream().filter(subTask -> subTask.getStartTime() != null).forEach(setTask::add);
        // epics.values().stream().filter(epic -> epic.getStartTime() != null).forEach(setTask::add);

        return setTask;
    }

    protected boolean intersectionTaskTime(Task t1) {
        for (Task t2 : getPrioritizedTasks()) {
            if ((
                    t1.getStartTime().isBefore(t2.getStartTime()) ||
                            t1.getStartTime().equals(t2.getStartTime()) ||
                            (t1.getStartTime().isAfter(t2.getStartTime()) &&
                                    t1.getStartTime().isBefore(t2.getEndTime()))
            ) && (
                    t1.getEndTime().equals(t2.getEndTime()) ||
                            t1.getEndTime().isAfter(t2.getEndTime()) ||
                            (t1.getEndTime().isBefore(t2.getEndTime()) && t1.getEndTime().isAfter(t2.getStartTime()))
            )) {
                return true;
            }
        }
        return false;
    }

    protected void updateStatusEpic(Epic epic) {
        int statusIsNew = 0;
        int statusIsDone = 0;
        if (epic.getIdSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            for (int idS : epic.getIdSubTasks()) {
                Status status = subTasks.get(idS).getStatus();
                if (status == Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                } else if (status == Status.NEW) {
                    statusIsNew++;
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
    }

    protected void setStartTimeEpic(Epic epic) {
        Optional<LocalDateTime> optional = epic.getIdSubTasks().stream()
                .map(subTasks::get)
                .map(Task::getStartTime)
                .min(LocalDateTime::compareTo);
        optional.ifPresent(epic::setStartTime);
    }

    protected void setEndTimeEpic(Epic epic) {
        Optional<LocalDateTime> optional = epic.getIdSubTasks().stream()
                .map(subTasks::get)
                .map(Task::getEndTime)
                .max(LocalDateTime::compareTo);
        optional.ifPresent(epic::setEndTimeEpic);
    }

    protected void setDurationEpic(Epic epic) {
        if (epic.getIdSubTasks().size() == 0) {
            epic.setDuration("null");
        } else {
            Duration duration = Duration.between(epic.getStartTime(), epic.getEndTimeEpic());
            epic.setDurationEpic(duration);
        }
    }

    protected void updateEpicFields(Epic epic) {
        updateStatusEpic(epic);
        setStartTimeEpic(epic);
        setEndTimeEpic(epic);
        setDurationEpic(epic);
    }

    private int makeId() {
        return idCount++;
    }
}




