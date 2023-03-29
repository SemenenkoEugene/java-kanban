package manager;

import entity.Epic;
import entity.Status;
import entity.SubTask;
import entity.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Test
    public void shouldSaveTasksEpicsSubtasksHistory() {
        Path path = Path.of("src/resources/test.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        Task task = new Task("TaskName", "TaskDescription", 1, Status.NEW, LocalDateTime.now().withNano(0), 10);
        manager.createTask(task);
        Epic epic = new Epic("EpicName", "EpicDescription", 2, Status.NEW, LocalDateTime.now().withNano(0), 20);
        manager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", 3,
                Status.NEW, LocalDateTime.of(2023, 3, 29, 17, 44), 20, epic.getId());
        manager.createSubTask(subTask);
        manager.getTaskById(1);
        manager.getEpicById(2);
        assertEquals(List.of(task), manager.getListAllTasks());
        assertEquals(List.of(subTask), manager.getListSubTasks());
        assertEquals(List.of(epic), manager.getListAllEpic());
        assertEquals(List.of(task, epic), manager.getHistory());
    }

    @Test
    public void shouldLoadTasksEpicsSubtasksHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        Task task = fileBackedTasksManager.getTaskById(1);
        Epic epic = fileBackedTasksManager.getEpicById(2);
        SubTask subTask = fileBackedTasksManager.getSubTaskById(3);
        List<Task> history = fileBackedTasksManager.getHistory();
        assertEquals(List.of(task), fileBackedTasksManager.getListAllTasks());
        assertEquals(List.of(epic), fileBackedTasksManager.getListAllEpic());
        assertEquals(List.of(subTask), fileBackedTasksManager.getListSubTasks());
        assertEquals(history, fileBackedTasksManager.getHistory());

    }
}