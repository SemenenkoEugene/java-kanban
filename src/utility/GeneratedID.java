package utility;

import java.util.concurrent.atomic.AtomicInteger;
public class GeneratedID {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    private final int id;
    public GeneratedID() {
        id = COUNTER.getAndIncrement();
    }
    public int getId() {
        return id;
    }
}
