package com.yandex.app.model;

import com.yandex.app.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {
    private TaskManager taskManager;

    @BeforeEach
    public void createTest() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void deleteIDSubTaskFromListEpic() {
        Epic epic = new Epic("Test 1", "Test ");
        SubTask subTask1 = new SubTask("Test 2", "Test ", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        SubTask subTask2 = new SubTask("Test 3", "Test ", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 2, 0), 1);
        SubTask subTask3 = new SubTask("Test 4", "Test ", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 4, 0), 1);

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

    @Test
    public void equalsFieldEpicAndSubTaskTest() {
        // у эпика есть два конструктора.
        // первый на создание:
        Epic epic = new Epic("Test 1", "Test "); // id 1
        taskManager.putNewEpic(epic);
        // второй на перезапись:
        Epic epic1 = new Epic("Test 1", "Test ", 1);
        taskManager.updateEpic(epic1);

        // у сабтаски есть два конструктора.
        // первый на создание:
        SubTask subTask1 = new SubTask("Test 1", "Test ", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        taskManager.putNewSubTask(subTask1);
        // второй на перезапись:
        SubTask subtask2 = new SubTask("Test 1", "Test ", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0), 1, 2);
        taskManager.updateSubTask(subtask2);

        assertEquals(epic, epic1, "Эпики не равны, несмотря на идентичность полей.");
        assertEquals(subTask1, subtask2, "Сабтаски не равны, несмотря на идентичность полей.");
        assertNotEquals(epic.getClass(), subTask1.getClass(),
                "Эпик и Сабтаск равны, несмотря на различия классов.");
        assertNotEquals(epic1, subTask1,
                "Эпик и Сабтаск равны, несмотря на различия полей.");
    }
}