package manager;

import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.util.List;

public interface TaskManager {

    // получение списка всех задач
    List<Task> getListAllTasks();

    // получение списка всех эпиков
    List<Epic> getListAllEpic();

    // получение списка всех подзадач определенного эпика
    List<SubTask> getListAllTasksOfEpic(Epic epic);

    //получение списка всех подзадач
    List<SubTask> getListSubTasks();

    // получение подзадачи по Id
    SubTask getSubTaskById(Integer id);

    // получение задачи по Id
    Task getTaskById(Integer id);

    // получение эпика по Id
    Epic getEpicById(Integer id);

    // создание новой задачи
    Task createTask(Task task);

    // создание новой подзадачи
    SubTask createSubTask(SubTask subTask);

    //создание нового эпика
    Epic createEpic(Epic epic);

    // обновление задачи
    Task updateTaskById(Task task);

    // обновление подзадачи по Id
    void updateSubTaskById(SubTask subTask);

    // обновление эпика
    Epic updateEpicById(Epic epic);

    // удаление всех задач
    void deleteAllTasks();

    // удаление всех подзадач
    void deleteAllSubTasks();

    // удаление всех эпиков
    void deleteAllEpics();

    // удаление подзадачи по Id
    void deleteSubTaskById(Integer id);

    // удаление эпика по Id
    void deleteEpicById(Integer id);

    // удаление задачи по Id
    void deleteTaskById(Integer id);

    // Получение истории
    List<Task> getHistory();

}
