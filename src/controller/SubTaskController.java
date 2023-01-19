package controller;

import entity.Epic;
import entity.Status;
import entity.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SubTaskController {

    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private Integer counterIdSubTasks = 0;
    private EpicController epicController;

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public SubTaskController(EpicController epicController) {
        this.epicController = epicController;
    }

    // получение списка всех подзадач определенного эпика
    public ArrayList<SubTask> getListAllTasksOfEpic(Epic epic) {
        return epicController.getEpicById(epic.getId()).getSubTasks();
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
    public SubTask createSubTask(SubTask subTask, Epic epic) {

        SubTask newSubTask = new SubTask(
                subTask.getName(), subTask.getDescription(),
                subTask.getStatus(), subTask.getEpicId());
        if (!subTasks.containsKey(newSubTask.getId())) {
            subTasks.put(newSubTask.getId(), newSubTask);
            epicController.getEpicById(epic.getId()).getSubTasks().add(newSubTask);
            subTask.setStatus(Status.NEW);
        } else {
            System.out.println("Подзадача с таким Id уже существует");
            return null;
        }
        return newSubTask;
    }

    // обновление подзадачи по Id
    public SubTask updateSubTaskById(SubTask subTask) {
        SubTask updateSubTask = subTasks.get(subTask.getId());
        if (updateSubTask == null) {
            System.out.println("Подзадачи с таким Id не существует");
            return null;
        }
        updateSubTask.setDescription(subTask.getDescription());
        updateSubTask.setName(subTask.getName());
        updateSubTask.setStatus(subTask.getStatus());
        epicController.getEpicById(subTask.getEpicId()).getSubTasks().remove(updateSubTask);
        epicController.getEpicById(subTask.getEpicId()).getSubTasks().add(subTask);
        refreshStatus(subTask);
        return updateSubTask;
    }

    // обновление статуса эпика в зависимости от статуса подзадачи
    public void refreshStatus(SubTask subTask) {
        ArrayList<SubTask> subTaskOfStatusEpic = epicController.getEpicById(subTask.getEpicId()).getSubTasks();
        int counterNew = 0;
        int counterDone = 0;
        for (SubTask task : subTaskOfStatusEpic) {
            if (task.getStatus() == Status.NEW) {
                counterNew++;
            } else if (subTask.getStatus() == Status.DONE) {
                counterDone++;
            }
        }
        if (counterNew == subTaskOfStatusEpic.size()) {
            epicController.getEpicById(subTask.getEpicId()).setStatus(Status.NEW);
        } else if (counterDone == subTaskOfStatusEpic.size()) {
            epicController.getEpicById(subTask.getEpicId()).setStatus(Status.DONE);
        } else {
            epicController.getEpicById(subTask.getEpicId()).setStatus(Status.IN_PROGRESS);
        }
    }

    // удаление подзадачи по Id
    public SubTask deleteSubTaskById(Integer id) {
        SubTask deleteSubTask = subTasks.get(id);
        epicController.getEpicById(deleteSubTask.getEpicId()).getSubTasks().remove(deleteSubTask);
        subTasks.remove(id);
        epicController.updateEpicById(epicController.getEpicById(id));
        return deleteSubTask;
    }
}
