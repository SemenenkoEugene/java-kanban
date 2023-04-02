package manager;

import entity.Epic;
import entity.Status;
import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public static final Path path = Path.of("test.data.csv");
    File file = new File(String.valueOf(path));
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(file,Managers.getDefaultHistory());
    }


    @Test
    public void shouldSaveAndLoadEpicWithoutSubtasks() {
        Path path = Path.of("src/resources/epicWithoutSubtasks.csv");
        File file = new File(String.valueOf(path));
        Epic epic = new Epic("Epic", "EpicDescription", 1,
                Status.NEW, LocalDateTime.now().withNano(0), 10);
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        manager.createEpic(epic);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        Epic epicById = fileBackedTasksManager.getEpicById(epic.getId());
        assertEquals(epic,epicById);
        assertArrayEquals(Collections.emptyList().toArray(),epicById.getSubTasks().toArray());

    }

    @Test
    public void shouldSaveEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        assertEquals(Collections.emptyList(),fileBackedTasksManager.getHistory());
    }


    @Test
    public void shouldLoadEmptyListOfTasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.emptyList(),fileBackedTasksManager.getListAllTasks());
    }

    @Test
    public void shouldLoadEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.emptyList(),fileBackedTasksManager.getHistory());
    }
}