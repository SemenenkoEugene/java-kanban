package manager;

import entity.Epic;
import entity.Status;
import entity.SubTask;
import entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    protected Task createTask() {
        return new Task("TaskName", "TaskDescription", 1, Status.NEW, LocalDateTime.now(), 0);
    }

    protected Epic createEpic() {
        return new Epic("EpicName", "EpicDescription", 1, Status.NEW, LocalDateTime.now(), 0);
    }

    protected SubTask createSubTask(Epic epic) {
        return new SubTask("SubTaskNew",
                "SubTaskNewDescription", 1, Status.NEW, LocalDateTime.now(), 0, epic.getId());
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask();
        manager.createTask(task);
        List<Task> tasks = manager.getListAllTasks();
        assertNotNull(task.getStatus());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void shouldCreateEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        List<Epic> epics = manager.getListAllEpic();
        assertNotNull(epic.getStatus());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubTasks());
        assertEquals(List.of(epic), epics);
    }

    @Test
    public void shouldCreateSubtask() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        SubTask subTask = createSubTask(epic);
        manager.createSubTask(subTask);
        List<SubTask> subTasks = manager.getListSubTasks();
        assertNotNull(subTask.getStatus());
        assertEquals(epic.getId(), subTask.getEpicId());
        assertEquals(Status.NEW, subTask.getStatus());
        assertEquals(List.of(subTask), subTasks);
        assertEquals(subTask.getEpicId(), epic.getSubTasks().get(0).getEpicId());
    }


    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = createTask();
        manager.createTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTaskById(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        SubTask subTask = createSubTask(epic);
        manager.createSubTask(subTask);
        subTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTaskById(subTask);
        assertEquals(Status.IN_PROGRESS, manager.getSubTaskById(subTask.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateTaskStatusToInDone() {
        Task task = createTask();
        manager.createTask(task);
        task.setStatus(Status.DONE);
        manager.updateTaskById(task);
        assertEquals(Status.DONE, manager.getTaskById(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInDone() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInDone() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        SubTask subTask = createSubTask(epic);
        manager.createSubTask(subTask);
        subTask.setStatus(Status.DONE);
        manager.updateSubTaskById(subTask);
        assertEquals(Status.DONE, manager.getSubTaskById(subTask.getId()).getStatus());
        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    public void shouldDeleteAllTasks() {
        Task task = createTask();
        manager.createTask(task);
        manager.deleteAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getListAllTasks());
    }

    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.deleteAllEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getListAllEpic());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        SubTask subTask = createSubTask(epic);
        manager.createSubTask(subTask);
        manager.deleteAllSubTasks();
        assertTrue(epic.getSubTasks().isEmpty());
    }

    @Test
    public void shouldDeleteAllSubtasksByEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        SubTask subTask = createSubTask(epic);
        manager.createSubTask(subTask);
        manager.deleteSubTaskById(epic.getId());
        assertTrue(epic.getSubTasks().isEmpty());
        assertTrue(manager.getListAllTasksOfEpic(epic).isEmpty());
    }

    @Test
    public void shouldDeleteTaskById() {
        Task task = createTask();
        manager.createTask(task);
        manager.deleteTaskById(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getListAllTasks());
    }

    @Test
    public void shouldDeleteEpicById() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.deleteEpicById(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getListAllEpic());
    }

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        Task task = createTask();
        manager.createTask(task);
        manager.deleteTaskById(777);
        assertEquals(List.of(task), manager.getListAllTasks());
    }

    @Test
    public void shouldNotDeleteEpicIfBadId() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.deleteEpicById(777);
        assertEquals(List.of(epic), manager.getListAllEpic());
    }


    @Test
    public void shouldDoNothingIfTaskHashMapIsEmpty(){
        manager.deleteAllTasks();
        manager.deleteTaskById(999);
        assertEquals(0, manager.getListAllTasks().size());
    }

    @Test
    public void shouldDoNothingIfEpicHashMapIsEmpty(){
        manager.deleteAllEpics();
        manager.deleteEpicById(777);
        assertTrue(manager.getListAllEpic().isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenGetSubtaskByEpicIdIsEmpty() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        List<SubTask> subTasks = manager.getListAllTasksOfEpic(epic);
        assertTrue(subTasks.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListTasksIfNoTasks() {
        assertTrue(manager.getListAllTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListEpicsIfNoEpics() {
        assertTrue(manager.getListAllEpic().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListSubtasksIfNoSubtasks() {
        assertTrue(manager.getListSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskById(777));
    }

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicById(777));
    }

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubTaskById(777));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnEmptyHistoryIfTasksNotExist() {
        manager.getTaskById(777);
        manager.getSubTaskById(777);
        manager.getEpicById(777);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnHistoryWithTasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        SubTask subtask = createSubTask(epic);
        manager.createSubTask(subtask);
        manager.getEpicById(epic.getId());
        manager.getSubTaskById(subtask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(1, list.size());
        assertTrue(list.contains(subtask));
    }

}