package manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import controller.EpicController;
import controller.SubTaskController;
import controller.TaskController;
import entity.Epic;
import entity.SubTask;
import entity.Task;
import http.KVTaskClient;
import manager.FileBackedTasksManager;
import manager.HistoryManager;
import manager.Managers;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient kvTaskClient;
    private final static String KEY_TASKS = "tasks";
    private final static String KEY_SUBTASKS = "subtasks";
    private final static String KEY_EPICS = "epics";
    private final static String KEY_HISTORY = "history";
    private final static Gson gson = Managers.getGson();

    private final TaskController taskController = new TaskController();
    private final EpicController epicController = new EpicController();
    private final SubTaskController subTaskController = new SubTaskController(epicController);

    public HTTPTaskManager(File file, HistoryManager historyManager) throws IOException, InterruptedException {
        super(file, historyManager);
        kvTaskClient = new KVTaskClient(String.valueOf(file));

        JsonElement jsonTasks = JsonParser.parseString(kvTaskClient.load(KEY_TASKS));
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksAsJsonArray = jsonTasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonTasksAsJsonArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                this.createTask(task);
            }
        }

        JsonElement jsonSubTasks = JsonParser.parseString(kvTaskClient.load(KEY_SUBTASKS));
        if (!jsonSubTasks.isJsonNull()) {
            JsonArray jsonSubTasksAsJsonArray = jsonSubTasks.getAsJsonArray();
            for (JsonElement jsonSubTask : jsonSubTasksAsJsonArray) {
                SubTask subTask = gson.fromJson(jsonSubTask, SubTask.class);
                this.createSubTask(subTask);
            }
        }

        JsonElement jsonEpics = JsonParser.parseString(kvTaskClient.load(KEY_EPICS));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsAsJsonArray = jsonEpics.getAsJsonArray();
            for (JsonElement jsonEpic : jsonEpicsAsJsonArray) {
                Epic epic = gson.fromJson(jsonEpic, Epic.class);
                this.createEpic(epic);
            }
        }

        JsonElement jsonHistory = JsonParser.parseString(kvTaskClient.load(KEY_HISTORY));
        if (!jsonHistory.isJsonNull()) {
            JsonArray jsonHistoryAsJsonArray = jsonHistory.getAsJsonArray();
            for (JsonElement jsonId : jsonHistoryAsJsonArray) {
                int id = jsonId.getAsInt();
                if (this.taskController.getTaskById(id) == taskController.getTaskById(id)) {
                    this.getTaskById(id);
                } else if (this.subTaskController.getSubTaskById(id) == subTaskController.getSubTaskById(id)) {
                    this.getSubTaskById(id);
                } else if (this.epicController.getEpicById(id) == epicController.getEpicById(id)) {
                    this.getEpicById(id);
                }
            }
        }
    }

    @Override
    public void save() {
        kvTaskClient.put(KEY_TASKS, gson.toJson(taskController.getListAllTasks()));
        kvTaskClient.put(KEY_SUBTASKS, gson.toJson(subTaskController.getListSubTasks()));
        kvTaskClient.put(KEY_EPICS, gson.toJson(epicController.getListAllEpic()));
        kvTaskClient.put(KEY_HISTORY, gson.toJson(this.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }
}
