package com.yandex.app.model;

public class SubTask extends Task {
    private final int idEpic;

    public SubTask(String name, String description, Status status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, Status status, int idEpic, int id) {
        super(name, description, status);
        this.idEpic = idEpic;
        setId(id);
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