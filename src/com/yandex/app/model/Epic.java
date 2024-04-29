package com.yandex.app.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> idSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        setName(name);
        setDesc(description);
        setStatus(Status.NEW);
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public void addIdSubTasks(int idSubTask) {
        idSubTasks.add(idSubTask);
    }

    public List<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void clearSubtasks() {
        idSubTasks.clear();
    }

    public void deleteSubTask(Integer idSubTask) {
        idSubTasks.remove(idSubTask);
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