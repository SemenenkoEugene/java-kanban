package http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import entity.Epic;
import entity.SubTask;
import entity.Task;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final String PATH = "/tasks";
    private static final String URL = "http://localhost:";
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
        httpServer.createContext(PATH + "/subtask/epic", this::subtasksEpicHandler);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    private void subtasksEpicHandler(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response = "";
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();

        System.out.println("Обрабатывается запрос " + path + " с методом " + requestMethod);

        if ("GET".equals(requestMethod)) {
            String query = httpExchange.getRequestURI().getQuery();
            try {
                int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                Epic epicById = taskManager.getEpicById(id);
                statusCode = 200;
                response = gson.toJson(taskManager.getListAllTasksOfEpic(epicById));
            } catch (StringIndexOutOfBoundsException e) {
                response = "В запросе отсутствует id";
            } catch (NumberFormatException e) {
                response = "Неверный формат id";
            }
        }
        httpExchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(response.getBytes());
        }
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

    private void epicHandler(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response = "";

        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();

        System.out.println("Обрабатывается запрос " + path + " с методом " + requestMethod);

        switch (requestMethod) {
            case "GET" -> {
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getListAllEpic());
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                        Epic epicById = taskManager.getEpicById(id);
                        if (epicById != null) {
                            statusCode = 200;
                            response = gson.toJson(epicById);
                        } else {
                            response = "Эпик с данным id не найден";
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                }
            }
            case "POST" -> {
                try {
                    String body = readText(httpExchange);
                    Epic epic = gson.fromJson(body, Epic.class);
                    Integer id = epic.getId();
                    taskManager.createEpic(epic);
                    statusCode = 201;
                    response = "Эпик с id=" + id + " обновлен/создан";
                } catch (JsonSyntaxException e) {
                    response = "Неверный формат запроса";
                }
            }
            case "DELETE" -> {
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.deleteAll();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                        taskManager.deleteEpicById(id);
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                }
            }
            default -> response = "Некорректный запрос";
        }
        httpExchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            responseBody.write(response.getBytes());
        }
    }

    private void subtaskHandler(HttpExchange httpExchange) throws IOException {
        int statusCode = 400;
        String response = "";
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();

        System.out.println("Обрабатывается запрос " + path + " с методом " + requestMethod);

        switch (requestMethod) {
            case "GET" -> {
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getListSubTasks());
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                        SubTask subTaskById = taskManager.getSubTaskById(id);
                        if (subTaskById != null) {
                            response = gson.toJson(subTaskById);
                        } else {
                            response = "Подзадача с таким id не найдена";
                        }
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                }
            }
            case "POST" -> {
                try {
                    String body = readText(httpExchange);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    Integer id = subTask.getId();
                    taskManager.createSubTask(subTask);
                    statusCode = 201;
                    response = "Подзадача с id=" + id + " обновлена";
                } catch (JsonSyntaxException e) {
                    response = "Неверный формат запроса";
                }
            }
            case "DELETE" -> {
                String query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.deleteAll();
                    statusCode = 200;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                        taskManager.deleteSubTaskById(id);
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                }
            }
            default -> response = "Некорректный запрос";
        }
            httpExchange.sendResponseHeaders(statusCode, 0);
            try (OutputStream responseBody = httpExchange.getResponseBody()) {
                responseBody.write(response.getBytes());
            }
        }

        private void taskHandler (HttpExchange httpExchange) throws IOException {
            int statusCode = 400;
            String response = "";
            String requestMethod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().toString();

            System.out.println("Обрабатывается запрос " + path + " с методом " + requestMethod);

            switch (requestMethod) {
                case "GET" -> {
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query == null) {
                        statusCode = 200;
                        response = gson.toJson(taskManager.getListAllTasks());
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
                }
                case "POST" -> {
                    try (InputStream inputStream = httpExchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        Integer id = task.getId();
                        taskManager.createTask(task);
                        statusCode = 201;
                        response = "Задача с id=" + id + " обновлена/создана";
                    } catch (JsonSyntaxException e) {
                        response = "Неверный формат запроса";
                    }
                }
                case "DELETE" -> {
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query == null) {
                        taskManager.deleteAll();
                        statusCode = 200;
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
                            taskManager.deleteTaskById(id);
                            statusCode = 200;
                            response = "Задача с id=" + id + " удалена";
                        } catch (StringIndexOutOfBoundsException e) {
                            response = "В запросе отсутствует необходимый параметр id";
                        } catch (NumberFormatException e) {
                            response = "Неверный формат id";
                        }
                    }
                }
                default -> response = "Некорректный запрос";
            }

            httpExchange.sendResponseHeaders(statusCode, 0);

            try (OutputStream responseBody = httpExchange.getResponseBody()) {
                responseBody.write(response.getBytes());
            }

        }

        private void generalHandler (HttpExchange httpExchange) throws IOException {
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
            try (httpExchange; OutputStream responseBody = httpExchange.getResponseBody()) {
                responseBody.write(response.getBytes());
            }
        }

        public void start () {
            System.out.println("Стартуем сервер " + PORT);
            System.out.println(URL + PORT + PATH);
            httpServer.start();
        }

        public void stop () {
            httpServer.stop(0);
            System.out.println("Остановили сервер на порту " + PORT);
        }

        private String readText (HttpExchange httpExchange) throws IOException {
            return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        }

        private void sendText (HttpExchange httpExchange, String text) throws IOException {
            byte[] response = text.getBytes(StandardCharsets.UTF_8);
            httpExchange.getRequestHeaders().add("Content-Type", "application/json;charset=utf-8");
            httpExchange.sendResponseHeaders(200, response.length);
            httpExchange.getResponseBody().write(response);
        }
    }
