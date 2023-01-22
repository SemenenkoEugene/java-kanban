package controller;

import entity.Epic;
import entity.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class SubTaskController {

    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int counterIdSubTasks = 0;
    private EpicController epicController;

    public SubTaskController(EpicController epicController) {
        this.epicController = epicController;
    }

    // получение списка всех подзадач определенного эпика
    public ArrayList<SubTask> getListAllTasksOfEpic(Epic epic) {
        ArrayList<SubTask> result = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId().equals(epic.getId())) {
                result.add(subTask);
            }
        }
        return result;
    }

    // получение списка всех подзадач
    public ArrayList<SubTask> getListSubTasks() {
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
        subTask.setId(++counterIdSubTasks);
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
        subTasks.remove(deleteSubTask);
        updateEpic(deleteSubTask);
    }

    private void updateEpic(SubTask subTask) {
        Epic epicById = epicController.getEpicById(subTask.getEpicId());
        epicById.setSubTasks(getListAllTasksOfEpic(epicById));
        epicController.updateEpicById(epicById);
    }
}
