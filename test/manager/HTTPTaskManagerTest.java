package manager;

import http.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

class HTTPTaskManagerTest extends FileBackedTasksManagerTest {

    private  KVServer server;

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
}