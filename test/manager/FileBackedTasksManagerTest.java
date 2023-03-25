package manager;

import entity.Epic;
import entity.Status;
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
    public void shouldCorrectlySaveAndLoad() {
        Path path = Path.of("src/resources/test.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        Task task = new Task("TaskName", "TaskDescription", 1, Status.NEW, LocalDateTime.now().withNano(0), 10);
        manager.createTask(task);
        Epic epic = new Epic("EpicName", "EpicDescription", 2, Status.NEW, LocalDateTime.now().withNano(0), 20);
        manager.createEpic(epic);
        assertEquals(List.of(task), manager.getListAllTasks());
        assertEquals(List.of(epic), manager.getListAllEpic());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        assertEquals(Collections.EMPTY_LIST, manager.getListAllTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getListAllEpic());
        assertEquals(Collections.EMPTY_LIST, manager.getListSubTasks());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        Path path = Path.of("src/resources/emptyHistory.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}