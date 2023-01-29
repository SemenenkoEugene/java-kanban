package controller;

import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * класс содержит список методов для всех типов задач
 */
public class InMemoryTaskManager implements TaskManager {

    private final TaskController taskController = new TaskController();
    private final EpicController epicController = new EpicController();
    private final SubTaskController subTaskController = new SubTaskController(epicController);
    private final HistoryManager historyManager = Managers.getDefaultHistory();

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
    public ArrayList<SubTask> getListSubTasks() {
        return subTaskController.getListSubTasks();
    }

    // получение подзадачи по Id
    @Override
    public SubTask getSubTaskById(Integer id) {
        historyManager.add(subTaskController.getSubTaskById(id));
        return subTaskController.getSubTaskById(id);
    }

    // получение задачи по Id
    @Override
    public Task getTaskById(Integer id) {
        historyManager.add(taskController.getTaskById(id));
        return taskController.getTaskById(id);
    }

    // получение эпика по Id
    @Override
    public Epic getEpicById(Integer id) {
        historyManager.add(epicController.getEpicById(id));
        return epicController.getEpicById(id);
    }

    // создание новой задачи
    @Override
    public Task createTask(Task task) {
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
    }

    // удаление эпика по Id
    @Override
    public void deleteEpicById(Integer id) {
        epicController.deleteEpicById(id);
    }

    // удаление задачи по Id
    @Override
    public Task deleteTaskById(Integer id) {
        return taskController.deleteTaskById(id);
    }

    // получение истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
