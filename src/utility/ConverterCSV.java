package utility;

import entity.Task;
import manager.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class ConverterCSV {

    /**
     * метод сохранения менеджера истории в строковое представление
     *
     * @param manager
     * @return строковое представление менеджера истории
     */
    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            stringBuilder.append(task.getId()).append(",");
        }

        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * метод восстановления менеджера истории из строки
     *
     * @param value
     * @return список истории менеджера истории
     */
    public static List<Integer> historyFromString(String value) {
        List<Integer> recoveryList = new ArrayList<>();
        if (value != null) {
            String[] strings = value.split(",");

            for (String string : strings) {
                recoveryList.add(Integer.parseInt(string));
            }
        }
        return recoveryList;
    }
}
