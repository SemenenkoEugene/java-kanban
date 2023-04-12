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
            taskManager = Managers.getDefaultHTTP();
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
    public void shouldLoadTasks() throws IOException, InterruptedException {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");
        assertEquals(1,httpTaskManager.taskController.getListAllTasks().size());

    }
}