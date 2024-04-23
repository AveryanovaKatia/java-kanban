package com.yandex.app.service;
import com.yandex.app.model.*;

import java.io.*;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        //сохраняет текущее состояние менеджера в файл
        List<String> listAllTask = new ArrayList<>();

        if (tasks.size() > 0) {
            for (Task task : tasks.values()) {
                listAllTask.add(convertToString(task));
            }
        }
        if (epics.size() > 0) {
            for (Epic epic : epics.values()) {
                listAllTask.add(convertToString(epic));
            }
        }
        if (subTasks.size() > 0) {
            for (SubTask subTask : subTasks.values()) {
                listAllTask.add(convertToString(subTask));
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,description,status,epic;");
            for (String line : listAllTask) {
                writer.write(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    //восстанавливает данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(file);

        String fr = Files.readString(file.toPath());
        String[] lines = fr.split(";");
        int idCount = 1;
        if (lines.length > 1) {
            for (int i = 0; i < lines.length; i++) {
                if (i == 0) {
                    continue;
                }
                String line = lines[i];
                String[] str = line.split(",");
                if (str[1].equals(TipeTask.TASK.toString())) {
                    Task task = fromStringTask(line);
                    fbtm.tasks.put(task.getId(), task);
                } else if (str[1].equals(TipeTask.EPIC.toString())) {
                    Epic epic = fromStringEpic(line);
                    fbtm.epics.put(epic.getId(), epic);
                } else {
                    SubTask subTask = fromStringSubTask(line);
                    fbtm.subTasks.put(subTask.getId(), subTask);
                }
            }
        }

        for (int i : fbtm.tasks.keySet()) {
            if (idCount < i) {
                idCount = i;
            }
        }

        for (int i : fbtm.epics.keySet()) {
            if (idCount < i) {
                idCount = i;
            }
        }

        for (SubTask subTask : fbtm.subTasks.values()) {
            final Epic epic = fbtm.epics.get(subTask.getIdEpic());
            int id = subTask.getId();
            epic.addIdSubTasks(id);
            if (idCount < id) {
                idCount = id;
            }
        }

        fbtm.idCount = idCount;
        return fbtm;
    }

    // Преобразовывает задачу в строку
    private String convertToString(Task task) {
        return task.getId() + "," + TipeTask.TASK + "," + task.getName() + "," + task.getDesc() + ","
                + task.getStatus() + ";";
    }

    private String convertToString(Epic epic) {
        return epic.getId() + "," + TipeTask.EPIC + "," + epic.getName() + "," + epic.getDesc() + ","
                + epic.getStatus() + ";";
    }

    private String convertToString(SubTask subTask) {
        return subTask.getId() + "," + TipeTask.SUBTASK + "," + subTask.getName() + "," + subTask.getDesc() + ","
                + subTask.getStatus() + "," + subTask.getIdEpic() + ";";
    }

    // Преобразовывает строку в задачу
    private static Task fromStringTask(String value) {
        String[] str = value.split(",");
        return new Task(str[2], str[3], Status.valueOf(str[4]), Integer.parseInt(str[0]));
    }

    private static Epic fromStringEpic(String value) {
        String[] str = value.split(",");
        return new Epic(str[2], str[3], Status.valueOf(str[4]), Integer.parseInt(str[0]));
    }

    private static SubTask fromStringSubTask(String value) {
        String[] str = value.split(",");
        return new SubTask(str[2], str[3], Status.valueOf(str[4]), Integer.parseInt(str[5]), Integer.parseInt(str[0]));
    }


    @Override
    public void putNewTask(Task task) {
        super.putNewTask(task);
        save();
    }

    @Override
    public void putNewEpic(Epic epic) {
        super.putNewEpic(epic);
        save();
    }

    @Override
    public void putNewSubTask(SubTask subTask) {
        super.putNewSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }
}
