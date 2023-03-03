import entity.Status;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Path of = Path.of("src/resources/data.csv");
//
        Path path = of;
        File file = new File(String.valueOf(path));
//        FileBackedTasksManager manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
//
//        Task task1 = new Task("Построить дом", "Возвести стены", Status.NEW);
//        manager.createTask(task1);
//
//        Task task2 = new Task("Посадить дерево", "Купить саженец", Status.NEW);
//        manager.createTask(task2);
//
//        Epic epic = new Epic("Магазин", "Купить продукты", Status.NEW);
//        manager.createEpic(epic);
//
//        SubTask subTask = new SubTask("Еда", "Составить список покупки еды", Status.NEW, epic.getId());
//        manager.createSubTask(subTask);
//
//        manager.getTaskById(task1.getId());
//        manager.getTaskById(task2.getId());
//        System.out.println();

        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(file);
        manager1.getListAllEpic().forEach(System.out::println);
        System.out.println();
        manager1.getListSubTasks().forEach(System.out::println);
        System.out.println();
        manager1.getListAllTasks().forEach(System.out::println);
        System.out.println();
        manager1.getHistory().forEach(System.out::println);


        //Спринт 5
//        TaskManager taskManager = Managers.getDefault();

//        taskManager.createTask(new Task("задача1", "описание1"));
//        taskManager.createTask(new Task("задача2", "описание2"));
//
//        Epic epic1 = new Epic("эпик1", "описание эпика1");
//        taskManager.createEpic(epic1);
//        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", epic1.getId());
//        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2", epic1.getId());
//        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", epic1.getId());
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//        taskManager.createSubTask(subTask3);
//
//        taskManager.getTaskById(1);
//
//        Epic epic2 = new Epic("эпик2", "описание эпика2");
//        taskManager.createEpic(epic2);
//        taskManager.getEpicById(7);
//        taskManager.getEpicById(3);


//        List<Task> history1 = taskManager.getHistory();
//        history1.forEach(System.out::println);
//        System.out.println("******************************");
//
//        taskManager.deleteTaskById(1);
//        List<Task> history2 = taskManager.getHistory();
//        history2.forEach(System.out::println);
//        System.out.println("******************************");
//
//        taskManager.deleteEpicById(3);
//        List<Task> history3 = taskManager.getHistory();
//        history3.forEach(System.out::println);


    }
}
