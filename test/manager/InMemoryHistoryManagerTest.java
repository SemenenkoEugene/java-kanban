package manager;

import entity.Epic;
import entity.Status;
import entity.SubTask;
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

    protected Epic createEpic() {
        return new Epic("EpicName", "EpicDescription", id, Status.NEW, LocalDateTime.now(), 0);
    }

    protected SubTask createSubTask(Epic epic) {
        return new SubTask("SubTaskNew",
                "SubTaskNewDescription", id, Status.NEW, LocalDateTime.now(), 0, epic.getId());
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
        assertEquals(Collections.emptyList(), manager.getHistory());
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
        assertEquals(Collections.emptyList(), manager.getHistory());
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

    @Test
    public void shouldHistoryIfRemoveFirstTask() {
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        manager.add(task1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        manager.add(task2);
        Task task3 = createTask();
        int newTaskId3 = generatedId();
        task3.setId(newTaskId3);
        manager.add(task3);
        manager.remove(task1.getId());
        assertEquals(List.of(task2, task3), manager.getHistory());
    }

    @Test
    public void shouldHistoryIfRemoveSecondTask() {
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        manager.add(task1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        manager.add(task2);
        Task task3 = createTask();
        int newTaskId3 = generatedId();
        task3.setId(newTaskId3);
        manager.add(task3);
        manager.remove(task2.getId());
        assertEquals(List.of(task1, task3), manager.getHistory());
    }

    @Test
    public void shouldHistoryIfRemoveThirdTask() {
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        manager.add(task1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        manager.add(task2);
        Task task3 = createTask();
        int newTaskId3 = generatedId();
        task3.setId(newTaskId3);
        manager.add(task3);
        manager.remove(task3.getId());
        assertEquals(List.of(task1, task2), manager.getHistory());
    }

    @Test
    public void shouldNotDuplicateHistory(){
        Task task1 = createTask();
        int newTaskId1 = generatedId();
        task1.setId(newTaskId1);
        manager.add(task1);
        Task task2 = createTask();
        int newTaskId2 = generatedId();
        task2.setId(newTaskId2);
        manager.add(task2);
        manager.add(task1);
        assertEquals(List.of(task2, task1), manager.getHistory());
    }

    @Test
    public void shouldReturnHistoryWithTasks() {
        Epic epic = createEpic();
        int newEpicId1 = generatedId();
        epic.setId(newEpicId1);
        manager.add(epic);
        SubTask subtask = createSubTask(epic);
        int newSubtaskId2 = generatedId();
        subtask.setId(newSubtaskId2);
        manager.add(subtask);
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(subtask));
    }

}