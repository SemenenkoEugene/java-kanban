package utility;

public class GeneratedID {
    private static int ID = 1;

    public static int getId() {
        return ID++;
    }

    public static void setId(int id) {
        if (GeneratedID.ID < id) {
            GeneratedID.ID = id;
        }
    }
}
