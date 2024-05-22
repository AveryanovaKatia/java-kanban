package com.yandex.app.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Epic;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.exception.NotFoundException;

import java.io.IOException;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
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
                        Epic epic = taskManager.getEpicById(id);
                        sendText(exchange, gson.toJson(epic));
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Эпика с id =" + id + " нет");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    try {
                        sendText(exchange, gson.toJson(taskManager.getAllEpic()));
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                }
                break;
            case "POST":
                Epic newEpic = gson.fromJson(readText(exchange), Epic.class);
                if (newEpic.getId() != 0) {
                    try {
                        taskManager.updateEpic(newEpic);
                        sendSuccess(exchange, "Эпик с id =" + id + " обновлен");
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Эпика с id =" + newEpic.getId() + " нет");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                } else {
                    try {
                        taskManager.putNewEpic(newEpic);
                        sendSuccess(exchange, "Эпик успешно добавлен");
                    } catch (Exception exception) {
                        internalServerError(exchange, exception.getMessage());
                    }
                }
                break;
            case "DELETE":
                if (idOpt.isPresent()) {
                    try {
                        id = idOpt.get();
                        taskManager.deleteEpicById(id);
                        sendSuccess(exchange, "Эпик с id =" + id + " удален");
                    } catch (NotFoundException notFoundException) {
                        sendNotFound(exchange, "Эпик не найдена");
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
