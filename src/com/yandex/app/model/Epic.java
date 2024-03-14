package com.yandex.app.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> idSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        setName(name);
        setDesc(description);
        setStatus(status);
    }

    public void setIdSubTasks(int idSubTask) {
        idSubTasks.add(idSubTask);
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void clearSubtasks() {
        idSubTasks.clear();
    }

    public void deleteSubTask(int idSubTask) {
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