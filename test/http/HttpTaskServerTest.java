package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.*;
import manager.Managers;
import org.junit.jupiter.api.*;

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

    private final HttpTaskServer taskServer;

    HttpClient client;
    private static final Gson gson = Managers.getGson();
    public static final String TASK_URL = "http://localhost:8080/tasks/task/";
    public static final String EPIC_URL = "http://localhost:8080/tasks/epic/";
    public static final String SUBTASK_URL = "http://localhost:8080/tasks/subtask/";
    public static final String BASE_URL = "http://localhost:8080/tasks/";
    public static final String HISTORY_URL = "http://localhost:8080/tasks/history/";
    public static final String SUBTASK_EPIC_URL = "http://localhost:8080/tasks/subtask/epic/";
    public static final String TASK_BY_ID_URL = "http://localhost:8080/tasks/task/?id=1";
    public static final String SUBTASK_BY_ID_URL = "http://localhost:8080/tasks/subtask/?id=3";
    public static final String EPIC_BY_ID_URL = "http://localhost:8080/tasks/epic/?id=2";

    Task task = new Task("Построить дом", "Возвести стены", 1,
            Status.NEW, LocalDateTime.of(2023, 4, 25, 14, 0), 50);
    Epic epic = new Epic("Магазин", "Купить продукты", 2,
            Status.NEW, LocalDateTime.of(2023, 3, 28, 9, 15, 0), 40);
    SubTask subTask = new SubTask("Еда", "Составить список покупки еды", 3,
            Status.NEW, LocalDateTime.of(2023, 5, 19, 11, 0), 50, epic.getId());

    HttpTaskServerTest() throws IOException {
        taskServer = new HttpTaskServer();
    }

    @BeforeEach
    public void resetServer() throws IOException, InterruptedException {
        taskServer.start();
        client = HttpClient.newHttpClient();
        deleteRequest();
        loadData(task, epic, subTask);
        addHistory(task, subTask);
    }

    private void deleteRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TASK_URL))
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @SafeVarargs
    private <T extends Task> void loadData(T... value) throws IOException, InterruptedException {
        for (T t : List.of(value)) {
            if (t instanceof SubTask) {
                URI url = URI.create(SUBTASK_URL);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } else if (t instanceof Epic) {
                URI url = URI.create(EPIC_URL);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } else {
                URI url = URI.create(TASK_URL);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(url)
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            }
        }
    }

    @SafeVarargs
    private <T extends Task> void addHistory(T... value) throws IOException, InterruptedException {
        for (T t : List.of(value)) {
            if (t instanceof SubTask) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(SUBTASK_URL + "/?id=" + t.getId()))
                        .GET()
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } else if (t instanceof Epic) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(EPIC_URL + "/?id=" + t.getId()))
                        .GET()
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } else {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(TASK_URL + "/?id=" + t.getId()))
                        .GET()
                        .build();
                client.send(request, HttpResponse.BodyHandlers.ofString());
            }
        }
    }

    @AfterEach
    public void stopServer() {
        taskServer.stop();
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
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
        assertEquals(1, actual.size());
    }

    @Test
    void shouldGetTasksById() throws IOException, InterruptedException {
        Task task = new Task("Построить дом", "Возвести стены", 1,
                Status.NEW, LocalDateTime.of(2023, 4, 25, 14, 0), 50);

        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
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
        assertEquals(1, actual.size());
    }

    @Test
    void shouldGetSubTasksById() throws IOException, InterruptedException {
        Epic epic = new Epic("Магазин", "Купить продукты", 2,
                Status.NEW, LocalDateTime.of(2023, 3, 28, 9, 15, 0), 40);
        SubTask subTask = new SubTask("Еда", "Составить список покупки еды", 3,
                Status.NEW, LocalDateTime.of(2023, 5, 19, 11, 0), 50, epic.getId());

        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
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

        URI url = URI.create(EPIC_BY_ID_URL);

        client = HttpClient.newHttpClient();
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
    }


    @Test
    void shouldGetForGetPrioritized() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
        URI url = URI.create(HISTORY_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Построить дом1", "Возвести стены", 1,
                Status.NEW, LocalDateTime.of(2023, 3, 25, 14, 0), 50);

        client = HttpClient.newHttpClient();
        URI url = URI.create(TASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Магазин", "Купить продукты", 2,
                Status.NEW, LocalDateTime.of(2023, 3, 28, 9, 15, 0), 40);

        client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldUpdateSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Магазин", "Купить продукты", 2,
                Status.NEW, LocalDateTime.of(2023, 3, 28, 9, 15, 0), 40);
        SubTask subTask = new SubTask("Еда1", "Составить список покупки еды1", 3,
                Status.NEW, LocalDateTime.of(2023, 3, 19, 11, 0), 50, epic.getId());

        client = HttpClient.newHttpClient();
        URI url = URI.create(SUBTASK_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void shouldDeleteTasks() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
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
        client = HttpClient.newHttpClient();
        URI url = URI.create(EPIC_BY_ID_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}