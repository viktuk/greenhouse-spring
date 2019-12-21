package xyz.viktuk.greenhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.viktuk.greenhouse.entity.CultivationParameter;
import xyz.viktuk.greenhouse.entity.Device;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.Unit;
import xyz.viktuk.greenhouse.entity.sensors.AbstractSensor;
import xyz.viktuk.greenhouse.utils.DeviceUtils;
import xyz.viktuk.greenhouse.utils.SensorsUtils;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitService {
    private ProjectService projectService;
    private SensorService sensorService;
    private CultivationParameterService cultivationParameterService;
    private DeviceService deviceService;

    public Unit getUnitById(Project project, BigInteger unitId) {
        if (unitId == null) {
            return null;
        }
        return project.getUnits().stream().filter(unit -> unit.getId().equals(unitId)).findFirst().orElse(null);
    }

    public Unit saveUnit(Project project, Unit unit) {
        if (getUnitById(project, unit.getId()) == null) {
            unit.setCreated(new Date());
            project.getUnits().add(unit);
        }
        projectService.saveProject(project);
        return unit;
    }

    public void deleteUnit(Project project, Unit unit) {
        if (unit == null || unit.getId() == null) {
            return;
        }
        List<AbstractSensor> sensorsToDelete = SensorsUtils.getSensorsByUnit(project, unit);
        for (AbstractSensor sensor : sensorsToDelete) {
            sensorService.deleteSensor(project, sensor);
        }

        List<CultivationParameter> cultivationParametersToDelete = project.getCultivationParameters().stream()
                .filter(cultivationParameter -> cultivationParameter.getUnit() != null && unit.getId().equals(cultivationParameter.getUnit().getId()))
                .collect(Collectors.toList());
        for (CultivationParameter cultivationParameter : cultivationParametersToDelete) {
            cultivationParameterService.deleteCultivationParameter(project, cultivationParameter);
        }

        List<Device> devicesToDelele = DeviceUtils.getAffectingDevices(project, unit);
        for(Device device: devicesToDelele){
            deviceService.deleteDevice(project, device);
        }

        project.getUnits().remove(unit);
        projectService.saveProject(project);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setSensorService(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Autowired
    public void setCultivationParameterService(CultivationParameterService cultivationParameterService) {
        this.cultivationParameterService = cultivationParameterService;
    }

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}
