package manager;

import entity.Status;
import entity.Task;
import http.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    private KVServer server;
    private TaskManager taskManager;

    @BeforeEach
    public void createManager() {

        try {
            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            taskManager = Managers.getInMemoryTaskManager(historyManager);
        } catch (IOException e) {
            System.out.println("Ошибка при создании менеджера");
        }
    }

    @AfterEach
    public void stopServer(){
        server.stop();
    }

    @Test
    public void shouldLoadTasks(){
        Task task1 = new Task("Task1", "TaskDescription1", 1,
                Status.NEW, LocalDateTime.of(2023, 4, 6, 14, 30), 20);
        Task task2 = new Task("Task2", "TaskDescription2", 2,
                Status.NEW, LocalDateTime.of(2023, 4, 6, 15, 0), 10);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        List<Task> history = manager.getHistory();
        assertEquals(manager.getListAllTasks(),history);

    }
}