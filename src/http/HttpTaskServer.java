package http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import entity.Task;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final String PATH = "/tasks";
    private static final String URL = "https://localhost:";

    private final HttpServer httpServer;
    private final Gson gson;

    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        gson = Managers.getGson();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext(PATH, this::generalHandler);
        httpServer.createContext(PATH + "/task", this::taskHandler);
        httpServer.createContext(PATH + "/subtask", this::subtaskHandler);
        httpServer.createContext(PATH + "/epic", this::epicHandler);
        httpServer.createContext(PATH + "/history", this::historyHandler);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    private void historyHandler(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response;
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();

        System.out.println("Обрабатывается запрос " + path + " с методом " + requestMethod);

        if ("GET".equals(requestMethod)) {
            statusCode = 200;
            response = gson.toJson(taskManager.getHistory());
        } else {
            response = "Некорректный запрос";
        }
        httpExchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(response.getBytes());
        }

    }

    private void epicHandler(HttpExchange httpExchange) {


    }

    private void subtaskHandler(HttpExchange httpExchange) {

    }

    private void taskHandler(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response = "";
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();

        System.out.println("Обрабатывается запрос " + path + " с методом " + requestMethod);

        switch (requestMethod) {
            case "GET": {
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = 200;
                    String json = gson.toJson(taskManager.getListAllTasks());
                    response = gson.toJson(json);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                        Task taskById = taskManager.getTaskById(id);
                        if (taskById != null) {
                            response = gson.toJson(taskById);
                        } else {
                            response = "Задача с данным id не найдена";
                        }
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                }
                break;
            }
            case "POST": {
                try (InputStream requestBody = httpExchange.getRequestBody()) {
                    String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    Integer id = task.getId();
                    if (taskManager.getTaskById(id) != null) {
                        taskManager.updateTaskById(task);
                        statusCode = 201;
                        response = "Задача с id=" + id + " обновлена";
                    } else {
                        Task createNewTask = taskManager.createTask(task);
                        Integer createNewTaskId = createNewTask.getId();
                        statusCode = 201;
                        response = "Создана задача с id=" + createNewTaskId;
                    }
                } catch (JsonSyntaxException e) {
                    response = "Неверный формат запроса";
                }
                break;
            }
            case "DELETE": {
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.deleteAll();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                        taskManager.deleteTaskById(id);
                        statusCode = 200;
                        response = "Задача с " + id + " удалена";
                    } catch (StringIndexOutOfBoundsException e) {
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                }
                break;
            }
            default: {
                response = "Некорректный запрос";
            }
        }

        httpExchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(response.getBytes());
        }

    }

    private void generalHandler(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response;
        String requestMethod = httpExchange.getRequestMethod();
        String uri = httpExchange.getRequestURI().toString();

        System.out.println("Обрабатывается запрос " + uri + " с методом " + requestMethod);

        if ("GET".equals(requestMethod)) {
            statusCode = 200;
            response = gson.toJson(taskManager.getPrioritizedTasks());
        } else {
            response = "Некорректный запрос";
        }
        httpExchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(response.getBytes());
        }


    }

    public void start() {
        System.out.println("Стартуем сервер " + PORT);
        System.out.println(URL + PORT + PATH);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getRequestHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, response.length);
        httpExchange.getResponseBody().write(response);
    }
}
