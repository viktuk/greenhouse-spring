package xyz.viktuk.greenhouse.enums;

public enum UnitAction {
    INCREASING(1),
    DECREASING(-1),
    EQUALS(0);

    private final int value;

    UnitAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UnitAction getUnitActionByString(String string) {
        if (string == null) {
            return null;
        }
        for (UnitAction action : UnitAction.values()) {
            if (action.name().equals(string.toUpperCase())) {
                return action;
            }
        }
        return null;
    }

    public static UnitAction getUnitActionByValue(int value) {
        for (UnitAction action : UnitAction.values()) {
            if (action.getValue() == value) {
                return action;
            }
        }
        return null;
    }
}
