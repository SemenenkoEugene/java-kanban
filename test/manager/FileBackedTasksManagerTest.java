package manager;

import entity.Epic;
import entity.Status;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Test
    public void shouldSaveEmptyListOfTasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        assertTrue(manager.getListAllTasks().isEmpty());
    }

    @Test
    public void shouldSaveEpicWithoutSubtasks() {
        Path path = Path.of("src/resources/epicWithoutSubtasks.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        Epic epic = new Epic("Epic", "EpicDescription", 1,
                Status.NEW, LocalDateTime.now().withNano(0), 10);
        manager.createEpic(epic);
        assertEquals(List.of(epic), manager.getListAllEpic());
    }

    @Test
    public void shouldSaveEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        assertTrue(manager.getHistory().isEmpty());
    }


    @Test
    public void shouldLoadEmptyListOfTasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertTrue(fileBackedTasksManager.getListAllTasks().isEmpty());
    }

    @Test
    public void shouldLoadEpicWithoutSubtasks() {
        Path path = Path.of("src/resources/epicWithoutSubtasks.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(List.of(fileBackedTasksManager.getEpicById(1)), fileBackedTasksManager.getListAllEpic());
        assertTrue(fileBackedTasksManager.getListSubTasks().isEmpty());
    }

    @Test
    public void shouldLoadEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertTrue(fileBackedTasksManager.getHistory().isEmpty());
    }
}