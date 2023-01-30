package controller;

import entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskController {

    private Map<Integer, Task> tasks = new HashMap<>();
    private int counterIdTasks = 0;

    // получение списка всех задач
    public List<Task> getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    // получение задачи по Id
    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    // создание новой задачи
    public Task createTask(Task task) {
        task.setId(++counterIdTasks);
        return tasks.put(task.getId(),task);
    }

    // обновление задачи
    public Task updateTaskById(Task task) {
        return tasks.put(task.getId(),task);
    }

    // удаление задачи по Id
    public Task deleteTaskById(Integer id) {
        Task deleteTask = tasks.get(id);
        tasks.remove(id);
        return deleteTask;
    }
}
