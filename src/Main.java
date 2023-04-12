import entity.Status;
import manager.FileBackedTasksManager;
import manager.Managers;
import entity.Epic;
import entity.SubTask;
import entity.Task;
import manager.TaskManager;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Path of = Path.of("src/resources/data.csv");

        Path path = of;
        File file = new File(String.valueOf(path));
        FileBackedTasksManager manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());

        Task task1 = new Task("Построить дом", "Возвести стены", Status.NEW, LocalDateTime.of(2023,3,25,14,0), 50);
        manager.createTask(task1);
//
//        Task task2 = new Task("Посадить дерево", "Купить саженец", Status.NEW, LocalDateTime.of(2023,3,26,17,50), 20);
//        manager.createTask(task2);
//
//        Task task3 = new Task("Посадить дерево1", "Купить саженец1", Status.NEW, LocalDateTime.of(2023,3,27,23,20), 30);
//        manager.createTask(task3);

        Epic epic = new Epic("Магазин", "Купить продукты", Status.NEW, LocalDateTime.of(2023,3,28,9,15,0), 40);
        manager.createEpic(epic);

        SubTask subTask = new SubTask("Еда", "Составить список покупки еды", Status.NEW, LocalDateTime.of(2023, 3, 19, 11, 0), 50, epic.getId());
        manager.createSubTask(subTask);
//
//        SubTask subTask1 = new SubTask("Еда2", "Составить список покупки еды2", Status.NEW, LocalDateTime.of(2023, 3, 19, 14, 0), 30, epic.getId());
//        manager.createSubTask(subTask1);
//
//        SubTask subTask2 = new SubTask("Еда3", "Составить список покупки еды3", Status.NEW, LocalDateTime.of(2023, 3, 19, 17, 0), 20, epic.getId());
//        manager.createSubTask(subTask2);
//
//        Epic epic1 = new Epic("Магазин1", "Купить продукты1", Status.NEW, LocalDateTime.of(2023,3,28,9,15,0), 40);
//        manager.createEpic(epic1);
//
//
//        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(file);
//        manager1.getListAllEpic().forEach(System.out::println);
//        System.out.println();
//        manager1.getListSubTasks().forEach(System.out::println);
//        System.out.println();
//        manager1.getListAllTasks().forEach(System.out::println);
//        System.out.println();
//        System.out.println("История");
//        manager1.getHistory().forEach(System.out::println);
//
//        System.out.println("--------------");
////
//        Task task = new Task("new Task", "new Description", Status.NEW);
//        manager1.createTask(task);
//        manager1.getListAllTasks().forEach(System.out::println);
//        SubTask subTask2 = new SubTask("new Subtask", "new Description", Status.NEW, 4);
//        manager1.createSubTask(subTask2);
//        manager1.getListSubTasks().forEach(System.out::println);
//        Epic epic = new Epic("new Epic","new Description", Status.NEW);
//        manager1.createEpic(epic);
//        manager1.getListAllEpic().forEach(System.out::println);

        //Спринт 5
//        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());

//        taskManager.createTask(new Task("задача1", "описание1"));
//        taskManager.createTask(new Task("задача2", "описание2"));
//
//        Epic epic1 = new Epic("эпик1", "описание эпика1");
//        Epic epic = taskManager.createEpic(epic1);
//        taskManager.getListAllEpic().forEach(System.out::println);
//        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", epic1.getId());
//        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2", epic1.getId());
//        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", epic1.getId());
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//        taskManager.createSubTask(subTask3);
//        taskManager.getListAllEpic().forEach(System.out::println);
//        System.out.println("-------------------------------------");


//        taskManager.getTaskById(1);
//
//        Epic epic2 = new Epic("эпик2", "описание эпика2");
//        taskManager.createEpic(epic2);
//        taskManager.getEpicById(7);
//        taskManager.getEpicById(3);
//
//
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
