package entity;

/**
 * класс содержит константы для установки статусов задачам
 */

public enum Status {
    NEW("new"),
    IN_PROGRESS("inProgress"),
    DONE("done");

    private final String stringStatus;

    Status(String stringStatus) {
        this.stringStatus = stringStatus;
    }

    public String getStringStatus() {
        return stringStatus;
    }

    @Override
    public String toString() {
        return stringStatus;
    }
}
