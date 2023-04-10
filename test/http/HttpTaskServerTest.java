package http;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import entity.Epic;
import entity.Status;
import entity.SubTask;
import entity.Task;
import manager.Managers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private static HttpTaskServer taskServer;
    private static final Gson gson = Managers.getGson();
    public static final String TASK_URL = "http://localhost:8080/tasks/task/";
    public static final String EPIC_URL = "http://localhost:8080/tasks/epic/";
    public static final String SUBTASK_URL = "http://localhost:8080/tasks/subtask/";
    public static final String BASE_URL = "http://localhost:8080/tasks/";
    public static final String HISTORY_URL = "http://localhost:8080/tasks/history/";
    public static final String SUBTASK_EPIC_URL = "http://localhost:8080/tasks/subtask/epic/";
    public static final String TASK_BY_ID_URL = "http://localhost:8080/tasks/task/?id=1";
    public static final String SUBTASK_BY_ID_URL = "http://localhost:8080/tasks/subtask/?id=5";

    public static final String EPIC_BY_ID_URL = "http://localhost:8080/tasks/epic/?id=8";

    @BeforeAll
    static void startServer() {
        try {
            taskServer = new HttpTaskServer();
            taskServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        taskServer.stop();
    }


    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);
        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldGetTasksById() throws IOException, InterruptedException {
        Task task = new Task("Построить дом", "Возвести стены", 1,
                Status.NEW, LocalDateTime.of(2023, 3, 25, 14, 0), 50);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);
        assertNotNull(actual);
        assertEquals(task, actual);
    }


    @Test
    void shouldGetSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SUBTASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> actual = gson.fromJson(response.body(), subTaskType);
        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldGetSubTasksById() throws IOException, InterruptedException {
        Epic epic = new Epic("Магазин", "Купить продукты", 4,
                Status.NEW, LocalDateTime.of(2023, 3, 28, 9, 15, 0), 40);

        SubTask subTask = new SubTask("Еда1", "Составить список покупки еды1", 5,
                Status.NEW, LocalDateTime.of(2023, 3, 19, 11, 0), 50, epic.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SUBTASK_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subTaskType = new TypeToken<SubTask>() {
        }.getType();
        SubTask actual = gson.fromJson(response.body(), subTaskType);
        assertNotNull(actual);
        assertEquals(subTask, actual);
    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldGetEpicsById() throws IOException, InterruptedException {
        Epic epic = new Epic("Магазин1", "Купить продукты1", 8,
                Status.NEW, LocalDateTime.of(2023, 3, 28, 9, 15, 0), 40);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);
        assertEquals(epic, actual);
    }


    @Test
    void shouldGetForGetPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldGetForGetHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(HISTORY_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteTasksById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SUBTASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteSubTasksById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(SUBTASK_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    @Test
    void shouldDeleteEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDeleteEpicsById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


}