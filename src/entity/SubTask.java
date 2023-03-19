package entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * класс описывает сущность задачи типа "подзадача"
 */

public class SubTask extends Task {

    private final Integer epicId;

    private LocalDateTime endTime;


    public SubTask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, LocalDateTime startTime, long duration, Integer epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
        this.endTime = super.getEndTime();
    }

    public SubTask(String name, String description, Integer id, Status status, LocalDateTime startTime, long duration, Integer epicId) {
        super(name, description, id, status, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicId, subTask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
               "epicId=" + epicId +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", id=" + id +
               ", status=" + status +
               ", startTime=" + startTime +
               ", endTime=" + getEndTime() +
               ", duration=" + duration +
               '}';
    }
}
