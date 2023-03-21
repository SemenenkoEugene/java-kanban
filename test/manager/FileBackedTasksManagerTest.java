package manager;

import entity.Epic;
import entity.Status;
import entity.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {


    public static final Path path = Path.of("src/resources/test.data.csv");
    File file = new File(String.valueOf(path));

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldCorrectlySaveAndLoad() {
        Task task = new Task("TaskName", "TaskDescription", 1, Status.NEW, LocalDateTime.now(), 0);
        manager.createTask(task);
        Epic epic = new Epic("EpicName", "EpicDescription", 2, Status.NEW, LocalDateTime.now(), 0);
        manager.createEpic(epic);
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(List.of(task), manager.getListAllTasks());
        assertEquals(List.of(epic), manager.getListAllEpic());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, manager.getListAllTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getListAllEpic());
        assertEquals(Collections.EMPTY_LIST, manager.getListSubTasks());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}