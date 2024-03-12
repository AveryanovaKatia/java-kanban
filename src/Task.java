import java.util.Objects;

class Task {
    protected String name;
    protected String description;
    Status status;
    int id;


    public Task(String name, String description, Status status) {
        setName(name);
        setDesk(description);
        setStatus(status);
    }

    Task() {
    }

    protected void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    protected void setDesk(String desk) {
        if (desk != null) {
            this.description = desk;
        }
    }

    protected void setStatus(Status status) {
        if (Status.NEW.equals(status) || Status.IN_PROGRESS.equals(status) || Status.DONE.equals(status)) {
            this.status = status;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;

        if (description != null) {
            hash = hash + description.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' + "}";
    }
}
