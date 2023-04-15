package manager;

import entity.Status;
import entity.Task;
import http.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

class HTTPTaskManagerTest extends FileBackedTasksManagerTest {

    private KVServer server;

    @BeforeEach
    public void createManager() {
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault();
        } catch (IOException e) {
            System.out.println("Ошибка при создании менеджера");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void loadTest() {
        Task task1 = new Task("Task1", "Description1",
                Status.NEW, LocalDateTime.of(2023, 4, 15, 19, 40), 15);
        Task task2 = new Task("Task2", "Description2",
                Status.NEW, LocalDateTime.of(2023, 4, 15, 20, 40), 15);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        List<Task> history = manager.getHistory();

        assertArrayEquals(List.of(task1, task2).toArray(), history.toArray());

    }
}