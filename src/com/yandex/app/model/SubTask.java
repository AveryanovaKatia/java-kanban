package com.yandex.app.model;

import com.yandex.app.service.*;

public class SubTask extends Task {
    private final int idEpic;

    public SubTask(String name, String description, Status status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SubTask otherSubTask = (SubTask) obj;
        // не понимаю как можно тут не переопределять, если нужно привести к типу SubTask
        return (id == otherSubTask.id);
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