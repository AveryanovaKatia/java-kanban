public class Main {

    public static void main(String[] args) {
        //Создала таск
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Приготовить ужин", "Паста и овощной салат", Status.NEW);
        taskManager.putNewTask(task1);
        //поменяла таск
        Task task2 = new Task("Приготовить ужин", "Паста и овощной салат", Status.DONE);
        taskManager.updateTask(task2);

        //создала второй таск
        Task task3 = new Task("Посмотреть фильм", "Любой, для настроения", Status.IN_PROGRESS);
        taskManager.putNewTask(task3);


        //создала первый эпик id 3
        Epic epic1 = new Epic("Курс по Java", "учиться и учиться!");
        taskManager.putNewEpic(epic1);

        //добавила сабтаск в эпик id 3
        SubTask subTask1 = new SubTask("Сдать все задания", "Успеть до дедлайна", Status.NEW);
        taskManager.putNewSubTask(subTask1, 3);
        //поменяла статус сабтаска в эпик id 3
        SubTask subTask4 = new SubTask("Сдать все задания", "Успеть до дедлайна", Status.DONE);
        taskManager.updateSubTask(subTask4, 3);


        //создала второй эпик id 5
        Epic epic2 = new Epic("Найти работу", "Очень важно!");
        taskManager.putNewEpic(epic2);

        //добавила два сабтаска в эпик id 5
        SubTask subTask2 = new SubTask("Составить резюме", "Согласно рекомндациям", Status.NEW);
        taskManager.putNewSubTask(subTask2, 5);
        SubTask subTask3 = new SubTask("Пройти собеседование", "Все получится!", Status.NEW);
        taskManager.putNewSubTask(subTask3, 5);


        taskManager.getOllTask();
        taskManager.getOllEpic();
        taskManager.getOllSubTask();
    }
}
