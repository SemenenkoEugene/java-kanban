package controller;

import entity.Epic;
import entity.Status;
import entity.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SubTaskController {

    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int counterIdSubTasks = 0;
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
        subTask.setId(++counterIdSubTasks);
        subTasks.put(subTask.getId(), subTask);
        refreshStatus(subTask);
        return subTask;
    }

    // обновление подзадачи по Id
    public SubTask updateSubTaskById(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        refreshStatus(subTask);
        return subTask;
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
        Epic epicControllerEpicById = epicController.getEpicById(subTask.getEpicId());
        ArrayList<SubTask> subTaskArrayList = new ArrayList<>();
        for (SubTask task : subTasks.values()){
            if (Objects.equals(task.getEpicId(), epicControllerEpicById.getId())) {
                subTaskArrayList.add(task);
            }
        }
        epicControllerEpicById.setSubTasks(subTaskArrayList);
        epicController.updateEpicById(epicControllerEpicById);
    }

    // удаление подзадачи по Id
    public void deleteSubTaskById(Integer id) {
        SubTask deleteSubTask = subTasks.get(id);
        epicController.getEpicById(deleteSubTask.getEpicId()).getSubTasks().remove(deleteSubTask);
        subTasks.remove(id);
        refreshStatus(subTasks.get(id));

    }
}
