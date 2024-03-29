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

    protected final TaskController taskController = new TaskController();
    protected final EpicController epicController = new EpicController();
    protected final SubTaskController subTaskController = new SubTaskController(epicController);
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
    );

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
        if (checkTime(task)) {
            throw new ManagerValidateException("Задача пересекается с уже существующей");
        }
        prioritizedTasks.add(task);
        return taskController.createTask(task);
    }

    // создание новой подзадачи
    @Override
    public SubTask createSubTask(SubTask subTask) {

        prioritizedTasks.add(subTask);
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
        Task oldTask = taskController.getTaskById(task.getId());
        prioritizedTasks.remove(oldTask);
        if (checkTime(task)) {
            prioritizedTasks.add(oldTask);
            throw new ManagerValidateException("Задача пересекается с уже существующей");
        }
        taskController.deleteTaskById(task.getId());
        prioritizedTasks.add(task);
        return taskController.updateTaskById(task);
    }

    // обновление подзадачи по Id
    @Override
    public void updateSubTaskById(SubTask subTask) {
        subTaskController.deleteSubTaskById(subTask.getId());
        prioritizedTasks.remove(subTask);
        prioritizedTasks.add(subTask);
//        validationTasks(subTask);
        subTaskController.updateSubTaskById(subTask);
    }

    // обновление эпика
    @Override
    public Epic updateEpicById(Epic epic) {
        return epicController.updateEpicById(epic);
    }

    //удаление всех задач, подзадач, эпиков
    @Override
    public void deleteAll() {
        prioritizedTasks.clear();
        taskController.deleteAllTasks();
        subTaskController.deleteAllSubTasks();
        epicController.deleteAllEpics();
    }

    @Override
    public void deleteAllTasks() {
        prioritizedTasks.removeIf(task -> task instanceof Task);
        taskController.deleteAllTasks();
    }

    @Override
    public void deleteAllEpics() {
        epicController.deleteAllEpics();
    }

    public void deleteSubTasks() {
        prioritizedTasks.removeIf(task -> task instanceof SubTask);
        subTaskController.deleteAllSubTasks();
    }

    // удаление подзадачи по Id
    @Override
    public void deleteSubTaskById(Integer id) {
        prioritizedTasks.removeIf(subtask -> Objects.equals(subtask.getId(), id));
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
        prioritizedTasks.removeIf(task -> Objects.equals(task.getId(), id));
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
        return new ArrayList<>(prioritizedTasks);
    }

    public void validationTasks(Task task) {
        boolean taskHasIntersections = checkTime(task);
        if (!taskHasIntersections) {
            throw new ManagerValidateException("Задача пересекается с уже существующей");
        }
    }

    private boolean checkTime(Task task) {
        List<Task> tasks = getPrioritizedTasks();
        for (Task taskCheck : tasks) {
            if (taskCheck.getStartTime() != null && taskCheck.getEndTime() != null) {
                if (
                        (taskCheck.getStartTime().equals(task.getStartTime())
                         && taskCheck.getEndTime().equals(task.getEndTime()))
                        || (taskCheck.getStartTime().isBefore(task.getStartTime())
                            && (taskCheck.getEndTime().isAfter(task.getStartTime()))
                            || (taskCheck.getStartTime().isAfter(task.getStartTime()))
                               && (taskCheck.getStartTime().isBefore(task.getEndTime())))
                ) {
                    return true;
                }
            }
        }
        return false;
    }
}
