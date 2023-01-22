import controller.Manager;
import controller.TaskManager;
import entity.Epic;
import entity.Status;
import entity.SubTask;
import entity.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Manager.getDefault();

        taskManager.createTask(new Task("задача1", "описание1"));
        taskManager.createTask(new Task("задача2", "описание2"));
        List<Task> listAllTasks = taskManager.getListAllTasks();
        System.out.println("listAllTasks = " + listAllTasks);

        Task task1 = taskManager.getTaskById(1);
        task1.setStatus(Status.DONE);
        taskManager.updateTaskById(task1);
        List<Task> listAllTasks1 = taskManager.getListAllTasks();
        System.out.println("listAllTasks1 = " + listAllTasks1);


        Epic epic1 = new Epic("эпик1", "описание эпика1");
        taskManager.createEpic(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2", epic1.getId());

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        System.out.println(taskManager.getListSubTasks());

        System.out.println(taskManager.getListAllEpic());

        SubTask subTaskById1 = taskManager.getSubTaskById(1);
        SubTask subTaskById2 = taskManager.getSubTaskById(2);
        subTaskById1.setStatus(Status.DONE);
        subTaskById2.setStatus(Status.DONE);
        taskManager.updateEpicById(epic1);
        System.out.println(taskManager.getListSubTasks());
        System.out.println(taskManager.getListAllEpic());

        taskManager.deleteSubTaskById(1);
        System.out.println(taskManager.getListSubTasks());

        Epic epic2 = new Epic("эпик2","описание эпика2");
        taskManager.createEpic(epic2);
        SubTask subTask3 = new SubTask("подзадача1","описание подзадачи1", epic2.getId());
        taskManager.createSubTask(subTask3);
        System.out.println(taskManager.getListSubTasks());
        System.out.println(taskManager.getListAllEpic());



    }
}
