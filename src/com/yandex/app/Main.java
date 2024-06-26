package com.yandex.app;

import com.yandex.app.service.*;
import com.yandex.app.model.*;
import com.yandex.app.service.exception.NotFoundException;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        //Создала таск
        Task task1 = new Task("Приготовить ужин", "Паста и овощной салат", Status.NEW,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0));
        taskManager.putNewTask(task1);
        //поменяла таск
        Task task2 = new Task("Приготовить ужин", "Паста и овощной салат", Status.DONE,
                "PT1H30M", LocalDateTime.of(2024, 1, 1, 0, 0), 1);
        taskManager.updateTask(task2);

        //создала второй таск
        Task task3 = new Task("Посмотреть фильм", "Любой, для настроения",
                Status.IN_PROGRESS, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 2, 0));
        taskManager.putNewTask(task3);


        //создала первый эпик id 3
        Epic epic1 = new Epic("Курс по Java", "учиться и учиться!");
        taskManager.putNewEpic(epic1);


        //добавила сабтаск в эпик id 3
        SubTask subTask1 = new SubTask("Сдать все задания", "Успеть до дедлайна",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 3);
        taskManager.putNewSubTask(subTask1);
        //поменяла статус сабтаска(4) в эпик id 3
        SubTask subTask4 = new SubTask("Сдать все задания", "Успеть до дедлайна",
                Status.DONE, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 3, 4);
        taskManager.updateSubTask(subTask4);

        System.out.println("_______");
        for (Task ticket : taskManager.getPrioritizedTasks()) {
            System.out.println("  * " + ticket);
        }
        System.out.println("_______");

        //создала второй эпик id 5
        Epic epic2 = new Epic("Найти работу", "Очень важно!");
        taskManager.putNewEpic(epic2);

        //добавила три сабтаска в эпик id 5
        SubTask subTask2 = new SubTask("Составить резюме", "Согласно рекомндациям",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 6, 0), 5);
        taskManager.putNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("Повторить изученное", "Набраться храбрости!",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 12, 0), 5);
        taskManager.putNewSubTask(subTask3);
        SubTask subTask5 = new SubTask("Пройти собеседование", "Все получится!",
                Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 14, 0), 5);
        taskManager.putNewSubTask(subTask5);

        //создала третий эпик id 9 без сабтасков
        Epic epic3 = new Epic("Тестирование", "Для истории");
        taskManager.putNewEpic(epic3);

        System.out.println("Задачи:");
        System.out.println(taskManager.getAllTask());

        System.out.println("Эпики:");
        System.out.println(taskManager.getAllEpic());

        System.out.println("Подзадачи:");
        System.out.println(taskManager.getAllSubTask());
        System.out.println();
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getEpicById(5));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getSubTaskById(7));
        System.out.println(taskManager.getSubTaskById(6));
        System.out.println(taskManager.getEpicById(9));
        System.out.println(taskManager.getEpicById(9));

        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(1);

        System.out.println(taskManager.getHistory());

        taskManager.deleteAllSubTask();

        System.out.println(taskManager.getHistory());
    }
}
