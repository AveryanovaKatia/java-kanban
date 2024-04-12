package com.yandex.app.model;

import com.yandex.app.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    private TaskManager taskManager;

    @BeforeEach
    public void addTaskTest() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void deleteIDSubTaskFromListEpic() {
        Epic epic = new Epic("Test 1", "Test ");
        SubTask subTask1 = new SubTask("Test 2", "Test ", Status.NEW, 1);
        SubTask subTask2 = new SubTask("Test 3", "Test ", Status.NEW, 1);
        SubTask subTask3 = new SubTask("Test 4", "Test ", Status.NEW, 1);

        taskManager.putNewEpic(epic);
        taskManager.putNewSubTask(subTask1);
        taskManager.putNewSubTask(subTask2);
        taskManager.putNewSubTask(subTask3);

        taskManager.deleteSubTaskById(2);

        List<Integer> list = epic.getIdSubTasks();
        assertEquals(2, list.size(), "id первого сабтаска удален.");

        taskManager.deleteAllSubTask();

        assertEquals(0, list.size(), "Все id сабтасков удалены.");
    }
}