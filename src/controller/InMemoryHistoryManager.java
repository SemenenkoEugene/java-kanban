package controller;

import entity.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> listTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (listTasks.size() == 10) {
            listTasks.remove(0);
        }
        listTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return listTasks;
    }
}
