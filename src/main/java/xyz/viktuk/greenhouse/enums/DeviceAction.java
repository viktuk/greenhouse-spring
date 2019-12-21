package xyz.viktuk.greenhouse.enums;

public enum DeviceAction {
    NOTHING,
    TURN_ON,
    TURN_OFF;

    public static DeviceAction getDeviceActionByString(String string) {
        if (string == null) {
            return null;
        }
        for (DeviceAction action : DeviceAction.values()) {
            if (action.name().equals(string.toUpperCase())) {
                return action;
            }
        }
        return null;
    }
}
