package com.yandex.app.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.exception.IntersectionException;
import com.yandex.app.service.exception.NotFoundException;

import java.io.IOException;

public class SubTaskHandler extends BaseHttpHandler {
    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getParameter(exchange);
        switch (endpoint) {
            case GET_BY_ID:
                try {
                    sendText(exchange, gson.toJson(taskManager.getSubTaskById(id)));
                } catch (NotFoundException notFoundException) {
                    sendNotFound(exchange, "Задачи с id =" + id + " нет");
                } catch (Exception exception) {
                    internalServerError(exchange, exception.getMessage());
                }
                break;
            case GET:
                try {
                    sendText(exchange, gson.toJson(taskManager.getAllSubTask()));
                } catch (Exception exception) {
                    internalServerError(exchange, exception.getMessage());
                }
                break;
            case POST_BY_ID:
                try {
                    taskManager.updateSubTask(newSubTask);
                    sendSuccess(exchange, "Задача с id =" + id + " обновлена");
                } catch (IntersectionException intersectionException) {
                    sendHasInteractions(exchange);
                } catch (NotFoundException notFoundException) {
                    sendNotFound(exchange, "Задачи с id =" + newSubTask.getId() + " нет");
                } catch (Exception exception) {
                    internalServerError(exchange, exception.getMessage());
                }
                break;
            case POST:
                try {
                    taskManager.putNewSubTask(newSubTask);
                    sendSuccess(exchange, "Задача успешно добавлена");
                } catch (IntersectionException intersectionException) {
                    sendHasInteractions(exchange);
                } catch (Exception exception) {
                    internalServerError(exchange, exception.getMessage());
                }
                break;
            case DELETE_BY_ID:
                try {
                    taskManager.deleteSubTaskById(id);
                    sendSuccess(exchange, "Задача с id =" + id + " удалена");
                } catch (NotFoundException notFoundException) {
                    sendNotFound(exchange, "Задача не найдена");
                } catch (Exception exception) {
                    internalServerError(exchange, exception.getMessage());
                }
                break;
            default:
                sendNotFound(exchange, "Такого эндпоинта нет");
        }
    }
}
