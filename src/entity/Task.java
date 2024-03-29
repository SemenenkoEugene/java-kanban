package entity;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * класс описывает сущность задачи типа "задача"
 */

public class Task {

    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected long duration;

    public  long SECOND_IN_MINUTE = 60L;
    protected TaskType taskType;

    public TaskType getTaskType() {
        return taskType;
    }



    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.MAX;
        this.duration = 0;
    }

    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Integer id, Status status, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusSeconds(duration * SECOND_IN_MINUTE);
        }
        return LocalDateTime.MAX;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(id, task.id) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", id=" + id +
               ", status=" + status +
               ", startTime=" + startTime +
               ", endTime=" + getEndTime() +
               ", duration=" + duration +
               '}';
    }
}
