
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int makeId() {
        return id++;
    }

    // добавть новую задачу
    public void putNewTask(Task task) {
        int id = makeId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void putNewEpic(Epic epic) {
        int id = makeId();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void putNewSubTask(SubTask subTask, int epicId) {
        int id = makeId();
        if (epics.containsKey(epicId)) {
            subTask.setIdEpic(epicId);
            subTask.setId(id);
            subTasks.put(id, subTask);
            epics.get(epicId).setSubTask(subTask); //not work
        }
    }

    //перезаписать задачу
    public void updateTask(Task task) {
        for (int id : tasks.keySet()) {
            if (task.equals(tasks.get(id))) {
                tasks.put(id, task);
                task.setId(id);
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (int id : epics.keySet()) {
            if (epic.equals(tasks.get(id))) {
                tasks.put(id, epic);
                epic.setId(id);
            }
        }
    }

    public void updateSubTask(SubTask subTask, int idEpic) {
       if (subTasks.containsValue(subTask)) {
           for (int id : subTasks.keySet()) {
               if (subTask.equals(subTasks.get(id))) {
                   subTask.setId(id);
                   subTask.setIdEpic(idEpic);
                   subTasks.put(id, subTask);
                   epics.get(idEpic).deleteSubTask(subTasks.get(id));
                   epics.get(idEpic).setSubTask(subTask);
                   epics.get(idEpic).setCalculatedStatus();
               }
           }
       }
    }

    //удалить все задачи
    public void deleteOllTask() {
        tasks.clear();
    }

    public void deleteOllEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteOllSubTask() {
        subTasks.clear();
    }

    //получить все задачи
    public void getOllTask() {
        for (Integer i : tasks.keySet()) {
            System.out.println(tasks.get(i).toString());
        }
    }

    public void getOllEpic() {
        for (Integer i : epics.keySet()) {
            System.out.println(epics.get(i).toString());
        }
    }

    public void getOllSubTask() {
        for (Integer i : subTasks.keySet()) {
            System.out.println(subTasks.get(i).toString());
        }
    }

    //получить задачу по id
    public String getTaskById(int id) {
        System.out.println(tasks.get(id).toString());
        return tasks.get(id).toString();
    }

    public String getEpicById(int id) {
        System.out.println(epics.get(id).toString());
        return epics.get(id).toString();
    }

    public String getSubTaskById(int id) {
        System.out.println(subTasks.get(id).toString());
        return subTasks.get(id).toString();
    }


    //удалить задачу по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        for (Integer key : subTasks.keySet()) {
            int idEpic = subTasks.get(key).getIdEpic();
            if (idEpic == id) {
                subTasks.remove(key);
            }
        }
        epics.remove(id);
    }

    public void deleteSubTaskById(int id) {
        int idEpic = subTasks.get(id).getIdEpic();
        epics.get(idEpic).deleteSubTask(subTasks.get(id));
        subTasks.remove(id);
    }

}





