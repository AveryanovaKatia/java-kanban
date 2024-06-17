package com.yandex.app.http;

import com.sun.net.httpserver.HttpServer;

import com.yandex.app.http.handlers.*;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager taskManager;
    private HttpServer httpServer;


    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subTasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));

        httpServer.start();
        System.out.println("Сервер трекер задач запущен на порту " + PORT);
    }

    public void stop() {
        int delay = 1;
        httpServer.stop(delay);
        System.out.println("Сервер трекер задач остановлен");
    }
}

