package controller;

import entity.Epic;
import entity.SubTask;
import utility.GeneratedID;

import java.util.*;

public class SubTaskController {

    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final EpicController epicController;

    public SubTaskController(EpicController epicController) {
        this.epicController = epicController;
    }

    // получение списка всех подзадач определенного эпика
    public List<SubTask> getListAllTasksOfEpic(Epic epic) {
        List<SubTask> result = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId().equals(epic.getId())) {
                result.add(subTask);
            }
        }
        return result;
    }

    // получение списка всех подзадач
    public List<SubTask> getListSubTasks() {
        if (subTasks.size() == 0) {
            return Collections.emptyList();
        }
        return new ArrayList<>(subTasks.values());
    }

    // удаление всех подзадач
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    // получение подзадачи по Id
    public SubTask getSubTaskById(Integer id) {
        return subTasks.get(id);
    }

    // создание новой подзадачи
    public SubTask createSubTask(SubTask subTask) {
        if (subTask.getId() == null) {
            subTask.setId(GeneratedID.getId());
        }
        subTasks.put(subTask.getId(), subTask);
        updateEpic(subTask);
        return subTask;
    }

    // обновление подзадачи по Id
    public void updateSubTaskById(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateEpic(subTask);
    }


    // удаление подзадачи по Id
    public void deleteSubTaskById(Integer id) {
        SubTask deleteSubTask = subTasks.get(id);
        if (deleteSubTask != null) {
            int epicId = deleteSubTask.getEpicId();
            subTasks.remove(id);
            Epic epicById = epicController.getEpicById(epicId);
            epicById.removeSubTask(deleteSubTask);
        }
    }

    private void updateEpic(SubTask subTask) {
        Epic epicById = epicController.getEpicById(subTask.getEpicId());
        if (epicById != null) {
            epicById.addSubTask(subTask);
        }
    }
}
