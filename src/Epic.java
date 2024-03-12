import java.util.ArrayList;
import java.util.Objects;

class Epic extends Task {
    ArrayList<SubTask> ollSubTask = new ArrayList<>();

    public Epic(String name, String description) {
        setName(name);
        setDesk(description);
        setCalculatedStatus();
    }

    public void setCalculatedStatus() {
        if (ollSubTask.isEmpty()) {
            setStatus(Status.NEW);
        } else {
            ArrayList<Status> statuses = new ArrayList<>();
            for (SubTask subTask : ollSubTask) {
                statuses.add(subTask.status);
            }
            if (!statuses.contains(Status.IN_PROGRESS) && !statuses.contains(Status.DONE)) {
                setStatus(Status.NEW);
            } else if (!statuses.contains(Status.NEW) && !statuses.contains(Status.IN_PROGRESS)) {
                setStatus(Status.DONE);
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void setSubTask(SubTask subTask) {
        ollSubTask.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        ollSubTask.remove(subTask);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(name, otherEpic.name) &&
                Objects.equals(description, otherEpic.description);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", subTask='" + ollSubTask + "}";
    }
}