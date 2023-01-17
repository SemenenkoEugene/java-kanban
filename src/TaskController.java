import java.util.ArrayList;
import java.util.HashMap;

public class TaskController {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private Integer counterIdTasks = 0;

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    // получение списка всех задач
    public ArrayList<Task> getListAllTasks() {
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
        Task newTask = new Task(task.getType(), task.getName(), task.getDescription(), ++counterIdTasks, task.getStatus());
        if (!tasks.containsKey(newTask.getId())) {
            tasks.put(newTask.getId(), newTask);
        } else {
            System.out.println("Задача с таким ID уже существует");
            return null;
        }
        return newTask;
    }

    // обновление задачи
    public Task updateTaskById(Task task) {
        Task updateTask = tasks.get(task.getId());
        if (updateTask == null) {
            System.out.println("Задача с таким ID уже существует");
            return null;
        }
        updateTask.setName(task.getName());
        updateTask.setDescription(task.getDescription());
        updateTask.setStatus(task.getStatus());
        return updateTask;
    }

    // удаление задачи по Id
    public Task deleteTaskById(Integer id) {
        Task deleteTask = tasks.get(id);
        tasks.remove(id);
        return deleteTask;
    }
}
