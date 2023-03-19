package manager;

import controller.*;
import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.util.*;

/**
 * класс содержит список методов для всех типов задач
 */
public class InMemoryTaskManager implements TaskManager {

    private final TaskController taskController = new TaskController();
    private final EpicController epicController = new EpicController();
    private final SubTaskController subTaskController = new SubTaskController(epicController);
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    public Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));


    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
    }

    // получение списка всех задач
    @Override
    public List<Task> getListAllTasks() {
        return taskController.getListAllTasks();
    }

    // получение списка всех эпиков
    @Override
    public List<Epic> getListAllEpic() {
        return epicController.getListAllEpic();
    }

    // получение списка всех подзадач определенного эпика
    @Override
    public List<SubTask> getListAllTasksOfEpic(Epic epic) {
        return subTaskController.getListAllTasksOfEpic(epic);
    }

    //получение списка всех подзадач
    @Override
    public List<SubTask> getListSubTasks() {

        return subTaskController.getListSubTasks();
    }

    // получение подзадачи по Id
    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTaskById = subTaskController.getSubTaskById(id);
        if (subTaskById != null) {
            historyManager.add(subTaskById);
        }
        return subTaskController.getSubTaskById(id);
    }

    // получение задачи по Id
    @Override
    public Task getTaskById(Integer id) {
        Task taskById = taskController.getTaskById(id);
        if (taskById != null) {
            historyManager.add(taskById);
        }
        return taskController.getTaskById(id);
    }

    // получение эпика по Id
    @Override
    public Epic getEpicById(Integer id) {
        Epic epicById = epicController.getEpicById(id);
        if (epicById != null) {
            historyManager.add(epicById);
        }
        return epicController.getEpicById(id);
    }

    // создание новой задачи
    @Override
    public Task createTask(Task task) {
        prioritizedTasks.add(task);
        validationTasks();
        return taskController.createTask(task);
    }

    // создание новой подзадачи
    @Override
    public SubTask createSubTask(SubTask subTask) {
        return subTaskController.createSubTask(subTask);
    }

    //создание нового эпика
    @Override
    public Epic createEpic(Epic epic) {
        return epicController.createEpic(epic);
    }

    // обновление задачи
    @Override
    public Task updateTaskById(Task task) {
        return taskController.updateTaskById(task);
    }

    // обновление подзадачи по Id
    @Override
    public void updateSubTaskById(SubTask subTask) {
        subTaskController.updateSubTaskById(subTask);
    }

    // обновление эпика
    @Override
    public Epic updateEpicById(Epic epic) {
        return epicController.updateEpicById(epic);
    }

    // удаление всех задач
    @Override
    public void deleteAllTasks() {
        taskController.deleteAllTasks();
    }

    // удаление всех подзадач
    @Override
    public void deleteAllSubTasks() {
        subTaskController.deleteAllSubTasks();
    }

    // удаление всех эпиков
    @Override
    public void deleteAllEpics() {
        epicController.deleteAllEpics();
    }

    // удаление подзадачи по Id
    @Override
    public void deleteSubTaskById(Integer id) {
        subTaskController.deleteSubTaskById(id);
        historyManager.remove(id);
    }

    // удаление эпика по Id
    @Override
    public void deleteEpicById(Integer id) {
        epicController.deleteEpicById(id);
        historyManager.remove(id);
    }

    // удаление задачи по Id
    @Override
    public void deleteTaskById(Integer id) {
        taskController.deleteTaskById(id);
        historyManager.remove(id);
    }

    // получение истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void addToHistory(int id) {
        if (epicController.getEpicById(id) != null) {
            historyManager.add(epicController.getEpicById(id));
        } else if (subTaskController.getSubTaskById(id) != null) {
            historyManager.add(subTaskController.getSubTaskById(id));
        } else if (taskController.getTaskById(id) != null) {
            historyManager.add(taskController.getTaskById(id));
        }
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    public List<Task> getPrioritizedTasks() {
        prioritizedTasks.addAll(taskController.getListAllTasks());
        prioritizedTasks.addAll(subTaskController.getListSubTasks());
        return new ArrayList<>(prioritizedTasks);
    }

    public void validationTasks() {
        List<Task> tasks = getPrioritizedTasks();
        for (int i = 1; i < tasks.size() - 1; i++) {
            Task task = tasks.get(i);
            boolean taskHasIntersections = isOverlapping(task);

            if (taskHasIntersections) {
                throw new ManagerValidateException(
                        "Задачи #" + task.getId() + " и #" + tasks.get(i - 1) + "пересекаются");
            }
        }
    }

    private boolean isOverlapping(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        int count = 0;
        if (tasks.size() > 0) {
            for (Task taskCheck : tasks) {
                if (taskCheck.getStartTime() != null && taskCheck.getEndTime() != null) {
                    if (task.getStartTime().isBefore(taskCheck.getStartTime())
                        && task.getEndTime().isBefore(taskCheck.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(taskCheck.getEndTime())
                               && task.getEndTime().isAfter(taskCheck.getEndTime())) {
                        return true;
                    }
                } else {
                    count++;
                }
            }
            return count == tasks.size();
        } else {
            return true;
        }

    }
}
