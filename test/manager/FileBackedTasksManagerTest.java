package manager;

import entity.Epic;
import entity.Status;
import entity.Task;
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
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        assertEquals(fileBackedTasksManager.getListAllTasks(), manager.getListAllTasks());


    }

    @Test
    public void shouldSaveEpicWithoutSubtasks() {
        Path path = Path.of("src/resources/epicWithoutSubtasks.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        Epic epic = new Epic("Epic", "EpicDescription", 1,
                Status.NEW, LocalDateTime.now().withNano(0), 10);
        Epic managerEpic = manager.createEpic(epic);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        Epic epicById = fileBackedTasksManager.getEpicById(1);
        assertEquals(epicById, managerEpic);

    }

    @Test
    public void shouldSaveEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        assertEquals(fileBackedTasksManager.getHistory(), manager.getHistory());
    }


    @Test
    public void shouldLoadEmptyListOfTasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        assertEquals(manager.getListAllTasks(),fileBackedTasksManager.getListAllTasks());
    }

    @Test
    public void shouldLoadEpicWithoutSubtasks() {
        Path path = Path.of("src/resources/epicWithoutSubtasks.csv");
        File file = new File(String.valueOf(path));
        Epic epic = new Epic("Epic", "EpicDescription", 1,
                Status.NEW, LocalDateTime.of(2023, 03, 30, 20, 58, 12), 10);
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        manager.createEpic(epic);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        Epic managerEpic = fileBackedTasksManager.createEpic(epic);
        assertEquals(manager.getEpicById(1), managerEpic);

    }

    @Test
    public void shouldLoadEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        manager = new FileBackedTasksManager(file, Managers.getDefaultHistory());
        assertEquals(manager.getHistory(),fileBackedTasksManager.getHistory());
    }
}