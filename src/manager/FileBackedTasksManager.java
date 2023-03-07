package manager;

import entity.*;
import utility.GeneratedID;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static utility.ConverterCSV.historyFromString;
import static utility.ConverterCSV.historyToString;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    /**
     * метод восстановления данных менеджера из файла
     *
     * @param file путь до файла
     */
    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        GeneratedID generatedID = new GeneratedID();


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()) {
                    break;
                }

                Task task = fromString(line);

                if (task instanceof Epic) {
                    tasksManager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    tasksManager.addSubTask((SubTask) task);
                } else if (task != null) {
                    tasksManager.addTask(task);
                } else {
                    System.out.println("Это не задача");
                }
                if (task != null) {
                    if (task.getId() > GeneratedID.getId()) {
                        generatedID.setId(GeneratedID.getId());
                    }
                }
            }

            String historyLine = bufferedReader.readLine();

            for (Integer id : historyFromString(historyLine)) {
                tasksManager.addToHistory(id);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать данные из файла", e);
        }
        return tasksManager;
    }

    /**
     * метод сохранения состояния менеджера в указанном файле
     */
    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task task : getListAllTasks()) {
                fileWriter.write(toString(task) + "\n");
            }

            for (Epic epic : getListAllEpic()) {
                fileWriter.write(toString(epic) + "\n");
            }

            for (SubTask subTask : getListSubTasks()) {
                fileWriter.write(toString(subTask) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка, файл не записался!", e);
        }
    }

    /**
     * метод получения вида задачи
     *
     * @param task принимает вид задачи
     * @return тип задачи
     */
    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof SubTask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    /**
     * метод сохранения задачи в строку
     *
     * @param task принимает вид задачи
     * @return строку вида "id,type,name,status,description,epic"
     */
    private String toString(Task task) {
        String[] arrayStringsCSV = {Integer.toString(task.getId()),
                getType(task).toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                getParentEpicId(task)};
        return String.join(",", arrayStringsCSV);
    }

    /**
     * метод преобразования эпика в строковое представление по его Id
     *
     * @param task
     * @return строковое представление Id эпика
     */
    private String getParentEpicId(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicId());
        }
        return "";
    }

    /**
     * метод создания задачи из строки
     *
     * @param value строковое представление входящих данных файла
     * @return вид задачи
     */
    private static Task fromString(String value) {
        String[] values = value.split(",");
        String id = values[0];
        String type = values[1];
        String name = values[2];
        String status = values[3];
        String description = values[4];
        Integer idOfEpic = type.equals(TaskType.SUBTASK.toString()) ? Integer.valueOf(values[5]) : null;
        switch (type) {
            case "EPIC":
                Epic epic = new Epic(name, description, Status.valueOf(status.toUpperCase()));
                epic.setId(Integer.parseInt(id));
                epic.setStatus(Status.valueOf(status.toUpperCase()));
                return epic;
            case "SUBTASK":
                SubTask subTask = new SubTask(name, description, Status.valueOf(status.toUpperCase()), idOfEpic);
                subTask.setId(Integer.parseInt(id));
                return subTask;
            case "TASK":
                Task task = new Task(name, description, Status.valueOf(status.toUpperCase()));
                task.setId(Integer.parseInt(id));
                return task;
            default:
                return null;
        }
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTaskById = super.getSubTaskById(id);
        save();
        return subTaskById;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epicById = super.getEpicById(id);
        save();
        return epicById;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask newSubTask = super.createSubTask(subTask);
        save();
        return newSubTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Task updateTaskById(Task task) {
        Task updateTaskById = super.updateTaskById(task);
        save();
        return updateTaskById;
    }

    @Override
    public void updateSubTaskById(SubTask subTask) {
        super.updateSubTaskById(subTask);
        save();
    }

    @Override
    public Epic updateEpicById(Epic epic) {
        Epic updateEpicById = super.updateEpicById(epic);
        save();
        return updateEpicById;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    public Task addTask(Task task) {
        return super.createTask(task);
    }

    public Epic addEpic(Epic epic) {
        return super.createEpic(epic);
    }

    public SubTask addSubTask(SubTask subTask) {
        return super.createSubTask(subTask);
    }
}