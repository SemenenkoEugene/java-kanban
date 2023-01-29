package entity;

import java.util.ArrayList;
import java.util.Objects;

/**
 * класс описывает сущность задачи типа "эпик"
 */

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    private boolean isStatus(ArrayList<SubTask> tasks, Status status) {
        for (SubTask task : tasks) {
            if (!task.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    // метод определения статуса эпика в зависимости от статуса подзадачи
    private void checkStatusEpic() {
        if (subTasks.isEmpty() || isStatus(subTasks, Status.NEW)) {
            status = Status.NEW;
        } else if (isStatus(subTasks, Status.DONE)) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    public void addSubTask(SubTask subTask){
        for (SubTask task : subTasks) {
            if(task.getId().equals(subTask.getId())) {
                subTasks.remove(task);
                break;
            }
        }
        subTasks.add(subTask);
        checkStatusEpic();
    }

    public void removeSubTask(SubTask subTask){
        subTasks.remove(subTask);
        checkStatusEpic();
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
        checkStatusEpic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
