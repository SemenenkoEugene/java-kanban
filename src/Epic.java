import java.util.ArrayList;

/**
 * класс описывает сущность задачи типа "эпик"
 */

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String type, String name, String description, Integer id, Status status) {
        super(type, name, description, id, status);
    }

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
