package com.yandex.app.model;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int idEpic;

    public SubTask(String name, String description, Status status, String duration, LocalDateTime startTime,
                   int idEpic) {
        super(name, description, status, duration, startTime);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, Status status, String duration, LocalDateTime startTime,
                   int idEpic, int id) {
        super(name, description, status, duration, startTime, id);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", idEpic='" + idEpic + '\'' + "}";
    }
}