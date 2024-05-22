package com.yandex.app.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.exception.IntersectionException;
import com.yandex.app.service.exception.NotFoundException;

import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        method = exchange.getRequestMethod();
        Optional<Integer> idOpt = getParameter(exchange);

        switch (method) {
            case "GET":
                if (idOpt.isPresent()) {
                    try {
                        id = idOpt.get();
                        Task task = taskManager.getTaskById(id);
                        sendText(exchange, gson.toJson(task));
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Задачи с id =" + id + " нет");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    try {
                        sendText(exchange, gson.toJson(taskManager.getAllTask()));
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                }
                break;
            case "POST":
                Task newTask = gson.fromJson(readText(exchange), Task.class);
                if (newTask.getId() != 0) {
                    try {
                        taskManager.updateTask(newTask);
                        sendSuccess(exchange, "Задача с id =" + id + " обновлена");
                    } catch (IntersectionException intersectionException) {
                        sendHasInteractions(exchange, "Задача пересекается по времени");
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Задачи с id =" + newTask.getId() + " нет");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    try {
                        taskManager.putNewTask(newTask);
                        sendSuccess(exchange, "Задача успешно добавлена");
                    } catch (IntersectionException intersectionException) {
                        sendHasInteractions(exchange, "Задача пересекается по времени");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                }
                break;
            case "DELETE":
                if (idOpt.isPresent()) {
                    try {
                        id = idOpt.get();
                        taskManager.deleteTaskById(id);
                        sendSuccess(exchange, "Задача с id =" + id + " удалена");
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Задача не найдена");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    sendNotFound(exchange, "не был передан id для удаления");
                }
            default:
                sendNotFound(exchange, "Такого эндпоинта нет");
        }
    }
}
