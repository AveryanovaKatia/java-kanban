package com.yandex.app.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.service.TaskManager;
import com.yandex.app.service.exception.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;
    protected String method;
    int id;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    protected Optional<Integer> getParameter(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] stringId = path.split("/");
        if (stringId.length > 2) {
            try {
                int idTask = Integer.parseInt(stringId[2]);
                return Optional.of(idTask);
            } catch (NumberFormatException e) {
                throw new NotFoundException("Идентификатор не является числом");
            }
        } else {
            return Optional.empty();
        }
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), TaskManager.DEFAULT_CHARSET);
    }

    protected void sendText(HttpExchange exchange, String responseString) throws IOException {
        byte[] resp = responseString.getBytes(TaskManager.DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendSuccess(HttpExchange exchange, String responseString) throws IOException {
        byte[] resp = responseString.getBytes(TaskManager.DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(201, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String responseString) throws IOException {
        byte[] resp = responseString.getBytes(TaskManager.DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    public void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(TaskManager.DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    //пока не поняла как использовать
    public void internalServerError(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(TaskManager.DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(500, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
