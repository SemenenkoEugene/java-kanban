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

        taskManager.createTask(new Task("задача1", "описание1"));
        taskManager.createTask(new Task("задача2", "описание2"));

        Epic epic1 = new Epic("эпик1", "описание эпика1");
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2", epic1.getId());
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", epic1.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        taskManager.getTaskById(1);

        Epic epic2 = new Epic("эпик2", "описание эпика2");
        taskManager.createEpic(epic2);
        taskManager.getEpicById(7);
        taskManager.getEpicById(3);

        List<Task> history1 = taskManager.getHistory();
        history1.forEach(System.out::println);
        System.out.println("******************************");

        taskManager.deleteTaskById(1);
        List<Task> history2 = taskManager.getHistory();
        history2.forEach(System.out::println);
        System.out.println("******************************");

        taskManager.deleteEpicById(3);
        List<Task> history3 = taskManager.getHistory();
        history3.forEach(System.out::println);

    }
}
