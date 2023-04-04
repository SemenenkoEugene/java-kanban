package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import entity.Task;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
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

    private void historyHandler(HttpExchange httpExchange) {

    }

    private void epicHandler(HttpExchange httpExchange) {

    }

    private void subtaskHandler(HttpExchange httpExchange) {

    }

    private void taskHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String[] parts = httpExchange.getRequestURI().toString().split("/");
        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            if ("GET".equals(requestMethod)) {
                if (parts.length == 3) {
                    List<Task> listAllTasks = taskManager.getListAllTasks();
                    String json = gson.toJson(listAllTasks);
                    httpExchange.sendResponseHeaders(200, 0);
                    responseBody.write(json.getBytes());
                } else if (parts.length == 4) {
                    int id = Integer.parseInt(parts[3].replace("?id=", ""));
                    Task taskById = taskManager.getTaskById(id);
                    String json = gson.toJson(taskById);
                    httpExchange.sendResponseHeaders(200, 0);
                    responseBody.write(json.getBytes());
                } else {
                    httpExchange.sendResponseHeaders(400,0);
                    responseBody.write("Неправильный метод".getBytes());
                }
            }
            if ("POST".equals(requestMethod)){

            }
            if ("DELETE".equals(requestMethod)){
                if (parts.length==3){


                }
            }
        }

    }

    private void generalHandler(HttpExchange httpExchange) {

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
