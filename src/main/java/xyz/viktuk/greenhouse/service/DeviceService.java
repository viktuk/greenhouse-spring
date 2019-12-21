package xyz.viktuk.greenhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.Device;

import java.math.BigInteger;
import java.util.Date;

@Service
public class DeviceService {
    private ProjectService projectService;

    public Device getDeviceById(Project project, BigInteger deviceId) {
        if (deviceId == null) {
            return null;
        }
        return project.getDevices().stream().filter(device -> device.getId().equals(deviceId)).findFirst().orElse(null);
    }

    public Device saveDevice(Project project, Device device) {
        if (getDeviceById(project, device.getId()) == null) {
            device.setCreated(new Date());
            project.getDevices().add(device);
        }
        projectService.saveProject(project);
        return device;
    }

    public void deleteDevice(Project project, Device device) {
        project.getDevices().remove(device);
        projectService.saveProject(project);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
