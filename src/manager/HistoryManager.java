package manager;

import entity.Task;

import java.util.List;

public interface HistoryManager {

    // Добавление задачи в историю.
    void add(Task task);

    // Удаление задачи из просмотра
    void remove(int id);

    // Получение истории.
    List<Task> getHistory();
}
