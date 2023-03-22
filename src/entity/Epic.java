package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * класс описывает сущность задачи типа "эпик"
 */

public class Epic extends Task {

    private List<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);

    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, long duration) {
        super(name, description, status, startTime, duration);
    }

    public Epic(String name, String description, Integer id, Status status, LocalDateTime startTime, long duration) {
        super(name, description, id, status, startTime, duration);
        this.endTime = getEndTime();
    }

    private boolean isStatus(List<SubTask> tasks, Status status) {
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

    private void updateTimeEpic() {
        getEndTimeEpic();
        getStartTimeEpic();
    }

    private long getDurationEpic() {
        int duration = 0;
        for (SubTask subTask : subTasks) {
            duration += subTask.duration;
        }
        return duration;
    }

    private LocalDateTime getEndTimeEpic() {
        if (subTasks.size() != 0) {
            LocalDateTime endTime = subTasks.get(0).getEndTime();
            for (SubTask subTask : subTasks) {
                if (subTask.getEndTime().isAfter(endTime)) {
                    endTime = subTask.getEndTime();
                }
            }
            return endTime;
        }
        return null;
    }

    private LocalDateTime getStartTimeEpic() {
        if (subTasks.size() != 0) {
            LocalDateTime startTime = subTasks.get(0).getStartTime();
            for (SubTask subTask : subTasks) {
                if (subTask.getStartTime().isBefore(startTime)) {
                    startTime = subTask.getStartTime();
                }
            }
            return startTime;
        } else {
            return null;
        }
    }

    public void addSubTask(SubTask subTask) {
        for (SubTask task : subTasks) {
            if (task.getId().equals(subTask.getId())) {
                subTasks.remove(task);
                break;
            }
        }
        subTasks.add(subTask);
        checkStatusEpic();
        updateTimeEpic();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        checkStatusEpic();
        updateTimeEpic();
    }

    @Override
    public LocalDateTime getEndTime() {
        return getEndTimeEpic();
    }

    public LocalDateTime getStartTime() {
        return getStartTimeEpic();
    }

    public long getDuration() {
        return getDurationEpic();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
        checkStatusEpic();
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
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
               ", startTime=" + getStartTime() +
               ", endTime=" + getEndTime() +
               ", duration=" + duration +
               '}';
    }
}
