package com.yandex.app.model;

import com.yandex.app.service.*;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks = new ArrayList<>();
    private ArrayList<Status> statusSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        setName(name);
        setDesc(description);
        setCalculatedStatus();
    }

    private void setCalculatedStatus() {
        if (idSubTasks.isEmpty()) {
            setStatus(Status.NEW);
        } else {
            if (!statusSubTasks.contains(Status.IN_PROGRESS) && !statusSubTasks.contains(Status.DONE)) {
                setStatus(Status.NEW);
            } else if (!statusSubTasks.contains(Status.NEW) && !statusSubTasks.contains(Status.IN_PROGRESS)) {
                setStatus(Status.DONE);
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void addSubTask(int idSubTask) {
        idSubTasks.add(idSubTask);
        setCalculatedStatus();
    }
    public ArrayList<Integer> getOllSubTask() {
        return new ArrayList<>(idSubTasks);
    }

    public void clearSubtasks() {
        idSubTasks.clear();
        setCalculatedStatus();
    }

    public void deleteSubTask(int idSubTask) {
        idSubTasks.remove(idSubTask);
        setCalculatedStatus();
    }

    public void makeStatusList (ArrayList<Status> statuses) {
        statusSubTasks.clear();
        statusSubTasks = statuses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        // не понимаю как можно тут не переопределять, если нужно привести к типу Epic
        return (id == otherEpic.id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", id subTask='" + idSubTasks + "}";
    }
}