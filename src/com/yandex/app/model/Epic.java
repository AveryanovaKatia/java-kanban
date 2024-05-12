package com.yandex.app.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> idSubTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        setName(name);
        setDesc(description);
        setStatus(Status.NEW);
    }

    public Epic(String name, String description, int id) {
        setName(name);
        setDesc(description);
        setId(id);
    }

    public Epic(String name, String description, Status status, String duration, LocalDateTime startTime, int id) {
        super(name, description, status, duration, startTime, id);
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

    public void setEndTimeEpic(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTimeEpic() {
        return endTime;
    }

    public void setDurationEpic(Duration duration) {
        this.duration = duration;
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