package com.yandex.app.service.file;
import com.yandex.app.model.*;
import com.yandex.app.service.inmemory.InMemoryTaskManager;
import com.yandex.app.service.exception.ManagerSaveException;
import com.yandex.app.service.TaskManager;

import java.io.*;
import java.nio.file.Files;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    //сохраняет текущее состояние менеджера в файл
    private void save() {
        List<String> listAllTask = new ArrayList<>();

        tasks.values().stream().map(this::convertToString).forEach(listAllTask::add);
        epics.values().stream().map(this::convertToString).forEach(listAllTask::add);
        subTasks.values().stream().map(this::convertToString).forEach(listAllTask::add);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,description,status,startTime,duration,epic;");
            for (String line : listAllTask) {
                writer.write(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    //восстанавливает данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

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
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                } else if (str[1].equals(TipeTask.EPIC.toString())) {
                    Epic epic = fromStringEpic(line);
                    fileBackedTaskManager.epics.put(epic.getId(), epic);
                } else {
                    SubTask subTask = fromStringSubTask(line);
                    fileBackedTaskManager.subTasks.put(subTask.getId(), subTask);
                }
            }
        }

        for (int i : fileBackedTaskManager.tasks.keySet()) {
            if (idCount < i) {
                idCount = i;
            }
        }

        for (int i : fileBackedTaskManager.epics.keySet()) {
            if (idCount < i) {
                idCount = i;
            }
        }

        for (SubTask subTask : fileBackedTaskManager.subTasks.values()) {
            final Epic epic = fileBackedTaskManager.epics.get(subTask.getIdEpic());
            int id = subTask.getId();
            epic.addIdSubTasks(id);
            if (idCount < id) {
                idCount = id;
            }
        }

        fileBackedTaskManager.idCount = idCount;

        for (Epic epic : fileBackedTaskManager.epics.values()) {
            Optional<LocalDateTime> optional = epic.getIdSubTasks().stream()
                    .map(fileBackedTaskManager.subTasks::get)
                    .map(Task::getEndTime)
                    .max(LocalDateTime::compareTo);
            optional.ifPresentOrElse(
                    LocalDateTime -> epic.setEndTimeEpic(optional.get()),
                    () -> epic.setEndTimeEpic(null)
            );
        }
        return fileBackedTaskManager;
    }

    // Преобразовывает задачу в строку
    private String convertToString(Task task) {
        return task.getId() + "," +
                TipeTask.TASK + "," +
                task.getName() + "," +
                task.getDesc() + "," +
                task.getStatus() + "," +
                task.getDuration().toString() + "," +
                task.getStartTime().format(formatter) + ";";
    }

    private String convertToString(Epic epic) {
        if (epic.getIdSubTasks().size() == 0) {
            return epic.getId() + "," +
                    TipeTask.EPIC + "," +
                    epic.getName() + "," +
                    epic.getDesc() + "," +
                    epic.getStatus() + ",null,null;";
        } else {
            return epic.getId() + "," +
                    TipeTask.EPIC + "," +
                    epic.getName() + "," +
                    epic.getDesc() + "," +
                    epic.getStatus() + "," +
                    epic.getDuration().toString() + "," +
                    epic.getStartTime().format(formatter) + ";";
        }
    }

    private String convertToString(SubTask subTask) {
        return subTask.getId() + "," +
                TipeTask.SUBTASK + "," +
                subTask.getName() + "," +
                subTask.getDesc() + "," +
                subTask.getStatus() + "," +
                subTask.getDuration().toString() + "," +
                subTask.getStartTime().format(formatter) + "," +
                subTask.getIdEpic() + ";";
    }

    // Преобразовывает строку в задачу
    private static Task fromStringTask(String value) {
        String[] str = value.split(",");
        LocalDateTime startTime = LocalDateTime.parse(str[6], formatter);
        return new Task(str[2], str[3], Status.valueOf(str[4]), str[5], startTime, Integer.parseInt(str[0]));
    }

    private static Epic fromStringEpic(String value) {
        String[] str = value.split(",");
        LocalDateTime startTime = LocalDateTime.parse(str[6], formatter);
        return new Epic(str[2], str[3], Status.valueOf(str[4]), str[5], startTime, Integer.parseInt(str[0]));
    }

    private static SubTask fromStringSubTask(String value) {
        String[] str = value.split(",");
        LocalDateTime startTime = LocalDateTime.parse(str[6], formatter);
        return new SubTask(str[2], str[3], Status.valueOf(str[4]), str[5], startTime,
                Integer.parseInt(str[7]), Integer.parseInt(str[0]));
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
