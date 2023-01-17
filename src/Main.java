import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println();

        TaskManager taskManager = Manager.getDefault();

        System.out.println("Метод createTask");
        Task task = new Task();
        System.out.println("Создаем две задачи");
        Task createdTask1 = taskManager.createTask(task);
        Task createdTask2 = taskManager.createTask(task);

        System.out.println("createdTask1 = " + createdTask1);
        System.out.println("createdTask2 = " + createdTask2);
        if (!task.equals(createdTask1) && !task.equals(createdTask2)) {
            System.out.println("Метод createTask работает правильно");
        } else {
            System.out.println("Метод createTask работает неправильно");
        }
        System.out.println();

        System.out.println("Метод getListAllTasks");
        List<Task> taskList = taskManager.getListAllTasks();
        System.out.println("Печатаем весь список задач");
        if (taskList != null) {
            for (Task tasks : taskList) {
                System.out.println(tasks);
            }
        } else {
            System.out.println("Список задач пуст");
        }
        if (taskList.isEmpty()) {
            System.out.println("Метод getListAllTasks не возвращает список задач");
        } else {
            System.out.println("Метод getListAllTasks работает правильно");
        }
        System.out.println();

        System.out.println("Метод getSubTaskById");
        Task getTask = taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        System.out.println("Печатаем полученную задачу");
        System.out.println(getTask);
        if (getTask.getId() != null) {
            System.out.println("Метод getSubTaskById работает правильно");
        } else {
            System.out.println("Метод getSubTaskById задачу не нашел");
        }
        System.out.println();

        System.out.println("Метод updateTaskById");
        Task updateTask = taskManager.updateTaskById(createdTask1);
        System.out.println("Печатаем переданную в метод и обновленную задачу");
        System.out.println("createdTask1 = " + createdTask1);
        System.out.println("updateTask = " + updateTask);
        if (updateTask.equals(createdTask1)) {
            System.out.println("Метод updateTaskById работает");
        } else {
            System.out.println("Метод updateTaskById  не работает");

        }
        System.out.println();

        System.out.println("Метод deleteTaskById");
        System.out.println("Печатаем удаляемую задачу");
        System.out.println(taskManager.getTaskById(1));
        taskManager.deleteTaskById(1);
        if (taskManager.deleteTaskById(1) == null) {
            System.out.println("Метод deleteTaskById работает правильно. Задача удалена");
        } else {
            System.out.println("Метод deleteTaskById не работает");
        }
        System.out.println();

        System.out.println("Метод deleteAllTasks");
        taskManager.deleteAllTasks();
        if (taskManager.getListAllTasks().isEmpty()) {
            System.out.println("Метод deleteAllTasks работает правильно");
        } else {
            System.out.println("Метод deleteAllTasks не работает");
        }
        System.out.println();

        System.out.println("Метод createEpic");
        Epic epic = new Epic("Эпик", "Описание эпика", -1);
        System.out.println("Создаем два эпика");
        Epic createEpic1 = taskManager.createEpic(epic);
        Epic createEpic2 = taskManager.createEpic(epic);
        System.out.println("Печатаем содержание эпиков");
        System.out.println("createEpic1 = " + createEpic1);
        System.out.println("createEpic2 = " + createEpic2);
        if (!epic.equals(createEpic1) && !epic.equals(createEpic2)) {
            System.out.println("Метод createEpic работает правильно");
        } else {
            System.out.println("Метод createEpic работает неправильно");
        }
        System.out.println();

        System.out.println("Метод createSubTask");
        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", -1, 1);
        System.out.println("Создаем и печатаем 2 подзадачи createEpic1");
        SubTask subTask1 = taskManager.createSubTask(subTask, createEpic1);
        SubTask subTask2 = taskManager.createSubTask(subTask, createEpic1);
        System.out.println("subTask1 = " + subTask1);
        System.out.println("subTask2 = " + subTask2);
        System.out.println("Создаем и печатаем 2 подзадачи createEpic2");
        SubTask subTask3 = taskManager.createSubTask(subTask, createEpic2);
        SubTask subTask4 = taskManager.createSubTask(subTask, createEpic2);
        System.out.println("subTask3 = " + subTask3);
        System.out.println("subTask4 = " + subTask4);
        if (subTask1.getEpicId().equals(subTask2.getEpicId()) && subTask3.getEpicId().equals(subTask4.getEpicId())) {
            System.out.println("Метод createSubTask работает правильно");
        } else {
            System.out.println("Метод createSubTask работает неправильно");

        }
        System.out.println();

        System.out.println("Метод updateSubTaskById");
        SubTask newSubTask = subTask1;
        System.out.println("Печатаем подзадачу до обновления");
        System.out.println("subTask1 = " + subTask1);
        newSubTask.setStatus(Status.DONE);
        taskManager.updateSubTaskById(newSubTask);
        System.out.println("Проверяем статус эпика");
        System.out.println(taskManager.getEpicById(1));
        System.out.println();

        System.out.println("Получение всех подзадач эпика");
        List<SubTask> listAllTasksOfEpic = taskManager.getListAllTasksOfEpic(taskManager.getEpicById(2));
        System.out.println("listAllTasksOfEpic = " + listAllTasksOfEpic);
        System.out.println("проверка обновления и удаления подзадач");
        SubTask subTask5 = new SubTask("Подзадача 5", "Описание подзадачи 5", 3, 2);
        SubTask subTask6 = new SubTask("Подзадача 6", "Описание подзадачи 6", 3, 2);
        subTask5.setStatus(Status.DONE);
        subTask6.setStatus(Status.DONE);
        taskManager.updateSubTaskById(subTask5);
        taskManager.updateSubTaskById(subTask6);
        System.out.println("listAllTasksOfEpic = " + listAllTasksOfEpic);
        System.out.println(taskManager.getEpicById(2));
        System.out.println(taskManager.getListAllTasksOfEpic(taskManager.getEpicById(2)));
        taskManager.deleteSubTaskById(3);
        System.out.println(taskManager.getListAllTasksOfEpic(taskManager.getEpicById(2)));
        System.out.println();

    }
}
