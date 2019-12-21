package xyz.viktuk.greenhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.sensors.AbstractSensor;
import xyz.viktuk.greenhouse.entity.sensors.ActiveSensor;
import xyz.viktuk.greenhouse.entity.sensors.PassiveSensor;
import xyz.viktuk.greenhouse.utils.SensorsUtils;

import java.math.BigInteger;
import java.util.Date;

@Service
public class SensorService {
    private ProjectService projectService;

    public AbstractSensor getSensorById(Project project, BigInteger sensorId) {
        if (sensorId == null) {
            return null;
        }
        return SensorsUtils.getAllSensors(project).stream().filter(sensor -> sensor.getId().equals(sensorId)).findFirst().orElse(null);
    }

    public AbstractSensor getActiveSensorById(Project project, BigInteger activeSensorId) {
        if (activeSensorId == null) {
            return null;
        }
        return project.getActiveSensors().stream().filter(sensor -> sensor.getId().equals(activeSensorId)).findFirst().orElse(null);
    }

    public AbstractSensor getPassiveSensorById(Project project, BigInteger passiveSensorId) {
        if (passiveSensorId == null) {
            return null;
        }
        return project.getPassiveSensors().stream().filter(sensor -> sensor.getId().equals(passiveSensorId)).findFirst().orElse(null);
    }

    public AbstractSensor saveSensor(Project project, AbstractSensor sensor) {
        if (sensor instanceof ActiveSensor && getActiveSensorById(project, sensor.getId()) == null) {
            sensor.setCreated(new Date());
            project.getActiveSensors().add((ActiveSensor) sensor);
        } else if (sensor instanceof PassiveSensor && getPassiveSensorById(project, sensor.getId()) == null) {
            sensor.setCreated(new Date());
            project.getPassiveSensors().add((PassiveSensor) sensor);
        }
        projectService.saveProject(project);
        return sensor;
    }

    public void deleteSensor(Project project, AbstractSensor sensor) {
        if (sensor instanceof ActiveSensor && getActiveSensorById(project, sensor.getId()) != null) {
            project.getActiveSensors().remove(sensor);
        } else if (sensor instanceof PassiveSensor && getPassiveSensorById(project, sensor.getId()) != null) {
            project.getPassiveSensors().remove(sensor);
        }
        projectService.saveProject(project);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
