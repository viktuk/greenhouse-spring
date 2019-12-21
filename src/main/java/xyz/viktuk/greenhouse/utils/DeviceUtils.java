package xyz.viktuk.greenhouse.utils;

import xyz.viktuk.greenhouse.entity.Device;
import xyz.viktuk.greenhouse.entity.DeviceBehaviour;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.Unit;
import xyz.viktuk.greenhouse.enums.DeviceAction;
import xyz.viktuk.greenhouse.enums.UnitAction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeviceUtils {
    public static List<Device> getAffectingDevices(Project project, Unit unit) {
        return project.getDevices().stream()
                .filter(device -> !getBehavioursByUnit(device, unit).isEmpty())
                .collect(Collectors.toList());
    }

    public static Map<UnitAction, DeviceAction> getBehavioursByUnit(Device device, Unit unit) {
        return device.getBehaviours().stream()
                .filter(deviceBehaviour -> deviceBehaviour.getUnit() != null
                        && unit.getId().equals(deviceBehaviour.getUnit().getId()))
                .collect(Collectors.toMap(DeviceBehaviour::getUnitAction, DeviceBehaviour::getDeviceAction));
    }
}
