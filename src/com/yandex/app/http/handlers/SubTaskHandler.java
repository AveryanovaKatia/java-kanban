package com.yandex.app.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.SubTask;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.exception.IntersectionException;
import com.yandex.app.service.exception.NotFoundException;

import java.io.IOException;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler {
    public SubTaskHandler(TaskManager taskManager) {
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
                        SubTask subTask = taskManager.getSubTaskById(id);
                        sendText(exchange, gson.toJson(subTask));
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Задачи с id =" + id + " нет");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    try {
                        sendText(exchange, gson.toJson(taskManager.getAllSubTask()));
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                }
                break;
            case "POST":
                SubTask newSubTask = gson.fromJson(readText(exchange), SubTask.class);
                if (newSubTask.getId() != 0) {
                    try {
                        taskManager.updateSubTask(newSubTask);
                        sendSuccess(exchange, "Задача с id =" + id + " обновлена");
                    } catch (IntersectionException intersectionException) {
                        sendHasInteractions(exchange, "Задача пересекается по времени");
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Задачи с id =" + newSubTask.getId() + " нет");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    try {
                        taskManager.putNewSubTask(newSubTask);
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
                        taskManager.deleteSubTaskById(id);
                        sendSuccess(exchange, "Задача с id =" + id + " удалена");
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Задача не найдена");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    sendNotFound(exchange, "не был передан id для удаления");
                }
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта нет");
        }
    }
}
