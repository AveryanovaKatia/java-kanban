import java.util.Objects;

class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    public void setIdEpic(int idEpic) {
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
        return Objects.equals(name, otherSubTask.name) &&
                Objects.equals(description, otherSubTask.description);
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