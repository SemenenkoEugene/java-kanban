package manager;

import entity.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private final File file;
    private HistoryManager historyManager;

    public FileBackedTasksManager(File file, HistoryManager historyManager) {
        this.file = file;
        this.historyManager = historyManager;
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    /**
     * метод восстановления данных менеджера из файла при запуске программы
     *
     * @param file путь до файла
     */
    public void loadFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()){
                    break;
                }
                Task task = fromString(line);
                if (task instanceof Epic) {
                    addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    addSubTask((SubTask) task);
                } else if (task != null){
                    addTask(task);
                } else {
                    System.out.println("Это не задача");

                }
            }
            String historyLine = bufferedReader.readLine();
            for (Integer id : historyFromString(historyLine)) {
                addToHistory(id);
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать данные из файла!");
        }
    }

    /**
     * метод сохранения состояния менеджера в указанном файле
     */
    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");

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
            System.out.println("Ошибка, файл не записался!");
        }
    }

    /**
     * метод сохранения менеджера истории в файл
     *
     * @param manager
     * @return строковое представление менеджера истории
     */
    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            stringBuilder.append(task.getId()).append(",");
        }

        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * метод восстановления менеджера истории из файла
     *
     * @param value
     * @return список истории менеджера истории
     */
    static List<Integer> historyFromString(String value) {
        List<Integer> recoveryList = new ArrayList<>();
        if (value != null) {
            String[] strings = value.split(",");

            for (String string : strings) {
                recoveryList.add(Integer.parseInt(string));
            }
            return recoveryList;
        }
        return recoveryList;
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
     * @return строковое представление эпика
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
        switch (values[1]) {
            case "EPIC":
                Epic epic = new Epic(values[2], values[4], Status.valueOf(values[3].toUpperCase()));
                epic.setId(Integer.parseInt(values[0]));
                epic.setStatus(Status.valueOf(values[3].toUpperCase()));
                return epic;
            case "SUBTASK":
                SubTask subTask = new SubTask(values[2], values[4], Status.valueOf(values[3].toUpperCase()), Integer.parseInt(values[5]));
                subTask.setId(Integer.parseInt(values[0]));
                return subTask;
            case "TASK":
                Task task = new Task(values[2], values[4], Status.valueOf(values[3].toUpperCase()));
                task.setId(Integer.parseInt(values[0]));
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

    public Task addTask(Task task) {
        return super.createTask(task);
    }

    public Epic addEpic(Epic epic) {
        return super.createEpic(epic);
    }

    public SubTask addSubTask(SubTask subTask) {
        return super.createSubTask(subTask);
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
}
