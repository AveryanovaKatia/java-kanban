package com.yandex.app.http.handles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yandex.app.http.HttpTaskServerTest;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.SubTask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskHandlerTest extends HttpTaskServerTest {
    @Test
    public void getSubTaskTest() throws InterruptedException, IOException {
        Epic epic1 = new Epic("Test Epic1", "Test");
        taskManager.putNewEpic(epic1);

        SubTask subTask1 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1);
        String taskJson = gson.toJson(subTask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        subTask1.setId(2);

        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subTasks/2")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым");

        SubTask taskOfGet = gson.fromJson(response.body(), SubTask.class);

        assertEquals(taskOfGet, subTask1,
                "Задача прошла конвертацию через Json не верно, или была получена не та задача");

        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subTasks/3")).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым");
    }

    @Test
    public void getSubTasksTest() throws InterruptedException, IOException {
        Epic epic1 = new Epic("Test Epic1", "Test");
        taskManager.putNewEpic(epic1);

        SubTask subTask1 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1);
        SubTask subTask2 = new SubTask("Test SubTask2", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 6, 0), 1);

        String taskJson1 = gson.toJson(subTask1);
        String taskJson2 = gson.toJson(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым");

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertTrue(jsonElement.isJsonArray(), "Получен не список");
        assertEquals(jsonArray.size(), 2, "Получено неверное количество задач");
    }

    @Test
    public void addSubTaskTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Test Epic1", "Test");
        taskManager.putNewEpic(epic1);

        SubTask subTask1 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1);

        String taskJson = gson.toJson(subTask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым");

        List<SubTask> tasksFromManager = taskManager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test SubTask1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");

        SubTask subTask2 = new SubTask("Test SubTask2", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 30), 1);

        String taskJson1 = gson.toJson(subTask2);

        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response1.statusCode(), "Код ответа не совпадает с ожидаемым");
    }

    @Test
    public void updateSubTaskTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Test Epic1", "Test");
        taskManager.putNewEpic(epic1);

        SubTask subTask1 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1);
        SubTask subTask2 = new SubTask("Test SubTask2", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 6, 0), 1, 2);

        String taskJson1 = gson.toJson(subTask1);
        String taskJson2 = gson.toJson(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        subTask1.setId(2);

        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым");

        List<SubTask> tasksFromManager = taskManager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void deleteSubTaskTest() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Test Epic1", "Test");
        taskManager.putNewEpic(epic1);

        SubTask subTask1 = new SubTask("Test SubTask1", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 4, 0), 1);
        SubTask subTask2 = new SubTask("Test SubTask2", "Test", Status.NEW, "PT1H30M",
                LocalDateTime.of(2024, 1, 1, 6, 0), 1);

        String taskJson1 = gson.toJson(subTask1);
        String taskJson2 = gson.toJson(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subTasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subTasks/2"))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым");

        List<SubTask> tasksFromManager = taskManager.getAllSubTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }
}
