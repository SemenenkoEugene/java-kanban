import java.util.ArrayList;
import java.util.HashMap;

public class EpicController {

    HashMap<Integer, Epic> epics = new HashMap<>();
    Integer counterIdEpics = 0;

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    // получение списка всех эпиков
    public ArrayList<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }

    // удаление всех эпиков
    public void deleteAllEpics() {
        epics.clear();
    }

    // получение эпика по Id
    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    //создание нового эпика
    public Epic createEpic(Epic epic) {
        Epic newEpic = new Epic(
                epic.getType(), epic.getName(), epic.getDescription(), ++counterIdEpics, epic.getStatus());
        if (!epics.containsKey(newEpic.getId())) {
            epics.put(newEpic.getId(), newEpic);
        } else {
            System.out.println("Эпик с таким Id уже существует");
            return null;
        }
        return newEpic;
    }

    // обновление эпика
    public Epic updateEpicById(Epic epic) {
        Epic updateEpic = epics.get(epic.getId());
        if (updateEpic == null) {
            System.out.println("Эпик с таким Id не существует");
            return null;
        }
        updateEpic.setName(epic.getName());
        updateEpic.setDescription(epic.getDescription());
        return updateEpic;
    }

    // удаление эпика по Id
    public void deleteEpicById(Integer id){
        epics.remove(id);
    }
}
