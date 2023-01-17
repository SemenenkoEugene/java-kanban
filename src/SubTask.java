import java.util.Objects;

/**
 * класс описывает сущность задачи типа "подзадача"
 */

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String type, String name, String description, Integer id, Status status, Integer epicId) {
        super(type, name, description, id, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Integer id, Integer epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
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
                ", type='" + type + '\'' +
                '}';
    }
}
