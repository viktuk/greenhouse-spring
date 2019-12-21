package xyz.viktuk.greenhouse.utils;

import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.Unit;
import xyz.viktuk.greenhouse.entity.sensors.AbstractSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SensorsUtils {

    public static List<AbstractSensor> getAllSensors(Project project) {
        List<AbstractSensor> sensors = new ArrayList<>();
        sensors.addAll(project.getActiveSensors());
        sensors.addAll(project.getPassiveSensors());
        return sensors;
    }


    public static List<AbstractSensor> getSensorsByUnit(Project project, Unit unit) {
        return getAllSensors(project).stream()
                .filter(sensor -> sensor.getUnit() != null && unit.getId().equals(sensor.getUnit().getId()))
                .collect(Collectors.toList());
    }

    public static List<AbstractSensor> getPassiveSensorsByUnit(Project project, Unit unit) {
        return project.getPassiveSensors().stream()
                .filter(sensor -> sensor.getUnit() != null && unit.getId().equals(sensor.getUnit().getId()))
                .collect(Collectors.toList());
    }
}
