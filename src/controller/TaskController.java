package controller;

import entity.Task;
import utility.GeneratedID;

import java.util.*;

public class TaskController {

    protected final Map<Integer, Task> tasks = new HashMap<>();

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
        if (task.getId() == null) {
            task.setId(GeneratedID.getId());
        }
        return tasks.put(task.getId(), task);
    }

    // обновление задачи
    public Task updateTaskById(Task task) {
        return tasks.put(task.getId(), task);
    }
    // удаление задачи по Id

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }


}
