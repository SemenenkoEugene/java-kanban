package manager;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTaskServer;
import http.KVServer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getInMemoryTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(new File("src/resources/data.csv"));
    }

    public static HTTPTaskManager getDefaultHTTP(HistoryManager historyManager) throws IOException, InterruptedException {
        return new HTTPTaskManager(new File("http://localhost:" + KVServer.PORT), historyManager);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
