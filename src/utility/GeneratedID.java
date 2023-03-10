package utility;

public class GeneratedID {
    private static int ID = 0;

    public static int getId() {
        return ++ID;
    }

    public static void setIfGreater(int id) {
        ID = id;
    }
}
