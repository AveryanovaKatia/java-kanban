package com.yandex.app.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            try {
                List<Task> history = taskManager.getHistory();
                sendText(exchange, gson.toJson(history));
            } catch (Exception exception) {
                internalServerError(exchange, exception.getMessage());
            }
        } else {
            sendNotFound(exchange, "Такого эндпоинта нет");
        }
    }
}
