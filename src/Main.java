import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        taskManager.createTask(new Task("задача1", "описание1"));
        taskManager.createTask(new Task("задача2", "описание2"));
        List<Task> listAllTasks = taskManager.getListAllTasks();
        listAllTasks.forEach(System.out::println);

        Task task1 = taskManager.getTaskById(4);
        Task task2 = taskManager.getTaskById(5);
//
        Epic epic1 = new Epic("эпик1", "описание эпика1");
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2", epic1.getId());
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", epic1.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        List<Epic> listAllEpic = taskManager.getListAllEpic();
        listAllEpic.forEach(System.out::println);
        Epic epicById1 = taskManager.getEpicById(6);

        Epic epic2 = new Epic("эпик2", "описание эпика2");
        taskManager.createEpic(epic2);
//
        historyManager.add(task1);
        historyManager.getHistory().forEach(System.out::println);
        System.out.println("**************************");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.getHistory().forEach(System.out::println);
        System.out.println("**************************");

        historyManager.remove(5);
        historyManager.getHistory().forEach(System.out::println);
        System.out.println("**************************");

        historyManager.add(epicById1);
        historyManager.getHistory().forEach(System.out::println);
        System.out.println();

        historyManager.remove(6);
        historyManager.getHistory().forEach(System.out::println);



    }
}
