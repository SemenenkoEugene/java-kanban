package http;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        new KVServer().start();
    }
}
