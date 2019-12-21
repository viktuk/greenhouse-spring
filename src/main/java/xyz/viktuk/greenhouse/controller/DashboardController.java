package xyz.viktuk.greenhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.viktuk.greenhouse.entity.*;
import xyz.viktuk.greenhouse.entity.sensors.AbstractSensor;
import xyz.viktuk.greenhouse.entity.sensors.ActiveSensor;
import xyz.viktuk.greenhouse.entity.sensors.PassiveSensor;
import xyz.viktuk.greenhouse.enums.DeviceAction;
import xyz.viktuk.greenhouse.enums.DeviceState;
import xyz.viktuk.greenhouse.enums.UnitAction;
import xyz.viktuk.greenhouse.service.*;
import xyz.viktuk.greenhouse.utils.TypesUtils;

import java.math.BigInteger;
import java.util.Map;

@Controller
public class DashboardController {
    private ProjectService projectService;
    private UnitService unitService;
    private CultivationParameterService cultivationParameterService;
    private SensorService sensorService;
    private DeviceService deviceService;

    @GetMapping("/dashboard/{id}/")
    public String getDashboard(Model model, @PathVariable BigInteger id) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "dashboard";
    }

    @PostMapping("/dashboard/{id}/createUnit")
    public String createUnit(@PathVariable BigInteger id,
                             @RequestParam String name,
                             @RequestParam String parameterName,
                             @RequestParam Integer defaultValue) {
        Project project = projectService.getProjectById(id);
        Unit unit = new Unit();
        unit.setName(name);
        unit.setParameterName(parameterName);
        unit.setDefaultValue(defaultValue);
        unitService.saveUnit(project, unit);
        return "redirect:./";
    }

    @GetMapping("/dashboard/{id}/deleteUnit")
    public String deleteUnit(@PathVariable BigInteger id, @RequestParam BigInteger unitId) {
        Project project = projectService.getProjectById(id);
        Unit unit = unitService.getUnitById(project, unitId);
        if (unit != null) {
            unitService.deleteUnit(project, unit);
        }
        return "redirect:./";
    }

    @PostMapping("/dashboard/{id}/createCultivationParameter")
    public String createCultivationParameter(@PathVariable BigInteger id, @RequestParam String name, @RequestParam BigInteger unitId,
                                             @RequestParam Long duration, @RequestParam Integer expectedValue) {
        Project project = projectService.getProjectById(id);
        Unit unit = unitService.getUnitById(project, unitId);
        CultivationParameter cultivationParameter = new CultivationParameter();
        cultivationParameter.setName(name);
        cultivationParameter.setUnit(unit);
        cultivationParameter.setDuration(duration);
        cultivationParameter.setExpectedValue(expectedValue);
        cultivationParameterService.saveCultivationParameter(project, cultivationParameter);
        return "redirect:./";
    }

    @GetMapping("/dashboard/{id}/deleteCultivationParameter")
    public String deleteCultivationParameter(@PathVariable BigInteger id, @RequestParam BigInteger cultivationParameterId) {
        Project project = projectService.getProjectById(id);
        CultivationParameter cultivationParameter = cultivationParameterService.getCultivationParameterById(project, cultivationParameterId);
        if (cultivationParameter != null) {
            cultivationParameterService.deleteCultivationParameter(project, cultivationParameter);
        }
        return "redirect:./";
    }

    @PostMapping("/dashboard/{id}/createActiveSensor")
    public String createActiveSensor(@PathVariable BigInteger id, @RequestParam String name, @RequestParam BigInteger unitId) {
        Project project = projectService.getProjectById(id);
        Unit unit = unitService.getUnitById(project, unitId);
        ActiveSensor activeSensor = new ActiveSensor();
        activeSensor.setName(name);
        activeSensor.setUnit(unit);
        activeSensor.setX(0);
        activeSensor.setY(0);
        sensorService.saveSensor(project, activeSensor);
        return "redirect:./";
    }

    @PostMapping("/dashboard/{id}/createPassiveSensor")
    public String createPassiveSensor(@PathVariable BigInteger id, @RequestParam String name, @RequestParam BigInteger unitId) {
        Project project = projectService.getProjectById(id);
        Unit unit = unitService.getUnitById(project, unitId);
        PassiveSensor passiveSensor = new PassiveSensor();
        passiveSensor.setName(name);
        passiveSensor.setUnit(unit);
        passiveSensor.setX(0);
        passiveSensor.setY(0);
        sensorService.saveSensor(project, passiveSensor);
        return "redirect:./";
    }

    @GetMapping("/dashboard/{id}/deleteSensor")
    public String deleteSensor(@PathVariable BigInteger id, @RequestParam BigInteger sensorId) {
        Project project = projectService.getProjectById(id);
        AbstractSensor sensor = sensorService.getSensorById(project, sensorId);
        if (sensor != null) {
            sensorService.deleteSensor(project, sensor);
        }
        return "redirect:./";
    }

    @PostMapping("/dashboard/{id}/createDevice")
    public String createDevice(@PathVariable BigInteger id, @RequestParam Map<String, String> requestParams) {
        Project project = projectService.getProjectById(id);
        Device device = new Device();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if ("name".equals(entry.getKey())) {
                device.setName(entry.getValue());
            } else if ("power".equals(entry.getKey())) {
                device.setPower(Integer.valueOf(entry.getValue()));
            } else {
                String[] parts = entry.getKey().split(":");
                if (parts.length != 2) {
                    continue;
                }
                UnitAction unitAction = UnitAction.getUnitActionByString(parts[0]);
                DeviceAction deviceAction = DeviceAction.getDeviceActionByString(entry.getValue());
                Unit unit = unitService.getUnitById(project, TypesUtils.getBigIntegerByString(parts[1]));
                if (unitAction == null || deviceAction == null || DeviceAction.NOTHING.equals(deviceAction) || unit == null) {
                    continue;
                }
                DeviceBehaviour deviceBehaviour = new DeviceBehaviour();
                deviceBehaviour.setUnitAction(unitAction);
                deviceBehaviour.setDeviceAction(deviceAction);
                deviceBehaviour.setUnit(unit);
                device.getBehaviours().add(deviceBehaviour);
            }
        }
        if (device.getBehaviours().isEmpty()) {
            return "redirect:./";
        }
        device.setX(0);
        device.setY(0);
        device.setState(DeviceState.OFF);
        deviceService.saveDevice(project, device);
        return "redirect:./";
    }

    @GetMapping("/dashboard/{id}/deleteDevice")
    public String deleteDevice(@PathVariable BigInteger id, @RequestParam BigInteger deviceId) {
        Project project = projectService.getProjectById(id);
        Device device = deviceService.getDeviceById(project, deviceId);
        if (device != null) {
            deviceService.deleteDevice(project, device);
        }
        return "redirect:./";
    }

    @RequestMapping(value = "/dashboard/{id}/savePosition", method = RequestMethod.POST)
    public @ResponseBody
    void savePosition(@PathVariable BigInteger id, @RequestParam Map<String, String> requestParams) {
        Project project = projectService.getProjectById(id);
        int x = 0, y = 0;
        String name = null;
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if ("name".equals(entry.getKey())) {
                name = entry.getValue();
            } else if ("x".equals(entry.getKey())) {
                x = Integer.parseInt(entry.getValue());
            } else if ("y".equals(entry.getKey())) {
                y = Integer.parseInt(entry.getValue());
            }
        }
        if (name == null) {
            return;
        }
        String[] parts = name.split("_");
        BigInteger point_id = new BigInteger(parts[parts.length - 1]);
        if (name.startsWith("device")) {
            Device device = deviceService.getDeviceById(project, point_id);
            device.setX(x);
            device.setY(y);
            deviceService.saveDevice(project, device);
        } else if (name.startsWith("active_sensor") || name.startsWith("passive_sensor")) {
            AbstractSensor sensor = sensorService.getSensorById(project, point_id);
            sensor.setX(x);
            sensor.setY(y);
            sensorService.saveSensor(project, sensor);
        }
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    @Autowired
    public void setCultivationParameterService(CultivationParameterService cultivationParameterService) {
        this.cultivationParameterService = cultivationParameterService;
    }

    @Autowired
    public void setSensorService(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}
