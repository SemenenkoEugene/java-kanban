package manager;

import entity.Status;
import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager manager;
    private int id = 0;

    public int generatedId() {
        return ++id;
    }

    protected Task createTask() {
        return new Task("TaskName", "TaskDescription", id, Status.NEW, LocalDateTime.now(), 0);
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        Task task3 = createTask();
        int newTaskId3 = generatedId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveTask() {
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        Task task3 = createTask();
        int newTaskId3 = generatedId();
        task3.setId(newTaskId3);
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.getId());
        assertEquals(List.of(task1, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task = createTask();
        int newTaskId = generatedId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldHistoryIsEmpty() {
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        Task task3 = createTask();
        int newTaskId3 = generatedId();
        task3.setId(newTaskId3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldNotRemoveTaskWithBadId() {
        Task task = createTask();
        int newTaskId = generatedId();
        task.setId(newTaskId);
        manager.add(task);
        manager.remove(0);
        assertEquals(List.of(task), manager.getHistory());
    }
}