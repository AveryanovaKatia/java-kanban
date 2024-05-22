package com.yandex.app.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            try {
                TreeSet<Task> setTask = taskManager.getPrioritizedTasks();
                sendText(exchange, gson.toJson(setTask));
            } catch (Exception exception) {
                internalServerError(exchange, exception.getMessage());
            }
        } else {
            sendNotFound(exchange, "Такого эндпоинта нет");
        }
    }
}


