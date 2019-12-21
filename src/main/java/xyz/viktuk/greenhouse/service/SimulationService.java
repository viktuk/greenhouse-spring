package xyz.viktuk.greenhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.viktuk.greenhouse.SimulationThread;
import xyz.viktuk.greenhouse.entity.Device;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.Unit;
import xyz.viktuk.greenhouse.entity.UnitEnvironment;
import xyz.viktuk.greenhouse.enums.DeviceState;
import xyz.viktuk.greenhouse.enums.SimulationState;
import xyz.viktuk.greenhouse.processor.SimulationProcessor;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static xyz.viktuk.greenhouse.Constants.ENVIRONMENT_SIZE;

@Service
public class SimulationService {
    private ProjectService projectService;
    private final Map<BigInteger, SimulationThread> simulationThreadMap = new HashMap<>();
    private SimulationProcessor simulationProcessor;

    public void startSimulation(Project project) {
        project.setSimulationState(SimulationState.STARTED);
        project.setTickCount(0L);
        for (Unit unit : project.getUnits()) {
            unit.setEnvironment(new UnitEnvironment(ENVIRONMENT_SIZE, ENVIRONMENT_SIZE, unit.getDefaultValue()));
        }
        for (Device device : project.getDevices()) {
            device.setState(DeviceState.OFF);
        }
        projectService.saveProject(project);
        SimulationThread simulationThread = new SimulationThread(project.getId(), simulationProcessor);
        simulationThreadMap.put(project.getId(), simulationThread);
        simulationThread.start();
    }

    public void stopSimulation(Project project) {
        SimulationThread simulationThread = simulationThreadMap.get(project.getId());
        simulationThread.interrupt();
        project.setSimulationState(SimulationState.STOPPED);
        for (Unit unit : project.getUnits()) {
            unit.setEnvironment(null);
        }
        project.setTickCount(0L);
        projectService.saveProject(project);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setSimulationProcessor(SimulationProcessor simulationProcessor) {
        this.simulationProcessor = simulationProcessor;
    }
}
