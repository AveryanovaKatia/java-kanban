package com.yandex.app.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskServerTest {
    protected TaskManager taskManager;
    protected HttpTaskServer taskServer;
    protected Gson gson;


    public HttpTaskServerTest() {
        taskManager = Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        }

        @BeforeEach
        public void setUp() throws IOException {
            taskManager.deleteAllTask();
            taskManager.deleteAllEpic();
            taskManager.deleteAllSubTask();
            taskServer.start();
        }

        @AfterEach
        public void shutDown() {
            taskServer.stop();
        }


}
