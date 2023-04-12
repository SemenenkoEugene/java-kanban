package manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import entity.Epic;
import entity.SubTask;
import entity.Task;
import http.KVTaskClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private final static String KEY_TASKS = "tasks";
    private final static String KEY_SUBTASKS = "subtasks";
    private final static String KEY_EPICS = "epics";
    private final static String KEY_HISTORY = "history";
    private final static Gson gson = Managers.getGson();

    public HTTPTaskManager(String path) throws IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(path);

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
                List<Task> taskList = taskController.getListAllTasks();
                List<SubTask> subTaskList = subTaskController.getListSubTasks();
                List<Epic> epicList = epicController.getListAllEpic();
                int id = jsonId.getAsInt();
                if (taskList.get(id) != null) {
                    this.getTaskById(id);
                } else if (subTaskList.get(id) != null) {
                    this.getSubTaskById(id);
                } else if (epicList.get(id) != null) {
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
