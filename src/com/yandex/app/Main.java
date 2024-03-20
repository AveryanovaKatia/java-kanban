package com.yandex.app;

import com.yandex.app.service.*;
import com.yandex.app.model.*;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        //Создала таск
        Task task1 = new Task("Приготовить ужин", "Паста и овощной салат", Status.NEW);
        taskManager.putNewTask(task1);
        //поменяла таск
        Task task2 = new Task("Приготовить ужин", "Паста и овощной салат", Status.DONE, 1);
        taskManager.updateTask(task2);

        //создала второй таск
        Task task3 = new Task("Посмотреть фильм", "Любой, для настроения",
                Status.IN_PROGRESS);
        taskManager.putNewTask(task3);


        //создала первый эпик id 3
        Epic epic1 = new Epic("Курс по Java", "учиться и учиться!");
        taskManager.putNewEpic(epic1);

        //добавила сабтаск в эпик id 3
        SubTask subTask1 = new SubTask("Сдать все задания", "Успеть до дедлайна",
                Status.NEW, 3);
        taskManager.putNewSubTask(subTask1);
        //поменяла статус сабтаска(4) в эпик id 3
        SubTask subTask4 = new SubTask("Сдать все задания", "Успеть до дедлайна",
                Status.DONE, 3, 4);
        taskManager.updateSubTask(subTask4);


        //создала второй эпик id 5
        Epic epic2 = new Epic("Найти работу", "Очень важно!");
        taskManager.putNewEpic(epic2);

        //добавила два сабтаска в эпик id 5
        SubTask subTask2 = new SubTask("Составить резюме", "Согласно рекомндациям",
                Status.NEW, 5);
        taskManager.putNewSubTask(subTask2);
        SubTask subTask3 = new SubTask("Пройти собеседование", "Все получится!",
                Status.NEW, 5);
        taskManager.putNewSubTask(subTask3);

        System.out.println("Задачи:");
        System.out.println(taskManager.getAllTask());

        System.out.println("Эпики:");
        System.out.println(taskManager.getAllEpic());

        System.out.println("Подзадачи:");
        System.out.println(taskManager.getAllSubTask());

        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getEpicById(3));

        System.out.println(taskManager.getHistory());

        System.out.println(Managers.getDefaultHistory().getHistory());

    }
}
