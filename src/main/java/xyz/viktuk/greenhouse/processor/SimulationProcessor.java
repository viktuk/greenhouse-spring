package xyz.viktuk.greenhouse.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.viktuk.greenhouse.entity.*;
import xyz.viktuk.greenhouse.entity.sensors.AbstractSensor;
import xyz.viktuk.greenhouse.enums.DeviceAction;
import xyz.viktuk.greenhouse.enums.DeviceState;
import xyz.viktuk.greenhouse.enums.UnitAction;
import xyz.viktuk.greenhouse.service.ProjectService;
import xyz.viktuk.greenhouse.utils.CultivationParameterUtils;
import xyz.viktuk.greenhouse.utils.DeviceUtils;
import xyz.viktuk.greenhouse.utils.SensorsUtils;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static xyz.viktuk.greenhouse.Constants.DISPERSION_RATIO;
import static xyz.viktuk.greenhouse.Constants.LOSS_PER_TICK;

@Component
public class SimulationProcessor {
    private ProjectService projectService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationProcessor.class);

    @Transactional
    public void tick(BigInteger projectId) {
        Project project = projectService.getProjectById(projectId);
        tick(project);
        projectService.saveProject(project);
    }

    private void tick(Project project) {
        for (Unit unit : project.getUnits()) {
            List<Device> devices = DeviceUtils.getAffectingDevices(project, unit).stream()
                    .filter(device -> device.getX() != null && device.getY() != null)
                    .collect(Collectors.toList());
            List<AbstractSensor> sensors = SensorsUtils.getSensorsByUnit(project, unit).stream()
                    .filter(sensor -> sensor.getX() != null && sensor.getY() != null)
                    .collect(Collectors.toList());
            recalculateEnvironment(unit, devices, sensors);
            //todo trigger only passive sensors one time per x ticks
            triggerDevices(unit, devices, sensors, CultivationParameterUtils.findCultivationParameter(project, unit));
        }
        project.setTickCount(project.getTickCount() + 1);
        LOGGER.info("Project {} ({}), Tick #{}", project.getName(), project.getId(), project.getTickCount());
    }

    private void recalculateEnvironment(Unit unit, List<Device> devices, List<AbstractSensor> sensors) {
        UnitEnvironment environment = unit.getEnvironment();
        devices = devices.stream().filter(device -> DeviceState.ON.equals(device.getState())).collect(Collectors.toList());
        for (int x = 0; x < environment.getMaxX(); x++) {
            for (int y = 0; y < environment.getMaxY(); y++) {
                PointImpl point = new PointImpl(x, y);
                int new_value = environment.getValue(x, y) + getInfluenceOfDevices(point, devices);
                if (new_value != unit.getDefaultValue()) {
                    new_value += (new_value < unit.getDefaultValue()) ? LOSS_PER_TICK : LOSS_PER_TICK * -1;
                }
                environment.setValue(x, y, new_value);
            }
        }
        for (AbstractSensor sensor : sensors) {
            sensor.setValue(environment.getValue(sensor.getX(), sensor.getY()));
        }
    }

    private int getInfluenceOfDevices(Point point, List<Device> devices) {
        int result = 0;
        for (Device device : devices) {
            result += getInfluenceOfDevice(point, device);
        }
        return result;
    }

    private int getInfluenceOfDevice(Point point, Device device) {
        int distance = getDistance(point, device);
        return distance == 0 ? device.getPower() : ((int) (device.getPower() * DISPERSION_RATIO)) / distance;
    }


    private void triggerDevices(Unit unit, List<Device> devices, List<AbstractSensor> sensors,
                                CultivationParameter cultivationParameter) {
        if (cultivationParameter == null) {
            return;
        }
        Map<Device, AbstractSensor> sensorPerDevice = new HashMap<>();
        for (Device device : devices) {
            AbstractSensor deviceSensor = sensors.stream()
                    .filter(sensor -> getInfluenceOfDevice(sensor, device) != 0)
                    .min(Comparator.comparingInt(sensor -> getDistance(sensor, device)))
                    .orElse(null);
            sensorPerDevice.put(device, deviceSensor);
        }
        for (Map.Entry<Device, AbstractSensor> entry : sensorPerDevice.entrySet()) {
            Device device = entry.getKey();
            AbstractSensor sensor = entry.getValue();
            if (sensor == null) {
                continue;
            }
            Map<UnitAction, DeviceAction> actionMap = DeviceUtils.getBehavioursByUnit(device, unit);
            UnitAction unitAction = UnitAction.getUnitActionByValue(sensor.getValue().compareTo(cultivationParameter.getExpectedValue()));
            DeviceAction deviceAction = actionMap.get(unitAction);
            if (deviceAction == null) {
                continue;
            }
            switch (deviceAction) {
                case TURN_ON: {
                    device.setState(DeviceState.ON);
                    break;
                }
                case TURN_OFF: {
                    device.setState(DeviceState.OFF);
                    break;
                }
            }
        }
    }

    private int getDistance(Point p1, Point p2) {
        int dx = p2.getX() - p1.getX();
        int dy = p2.getY() - p1.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
