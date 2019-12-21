package xyz.viktuk.greenhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.UnitEnvironment;
import xyz.viktuk.greenhouse.service.ProjectService;
import xyz.viktuk.greenhouse.service.SimulationService;
import xyz.viktuk.greenhouse.service.UnitService;

import java.math.BigInteger;

@Controller
public class SimulationController {
    private ProjectService projectService;
    private SimulationService simulationService;
    private UnitService unitService;

    @GetMapping("/simulation/{id}/")
    public String getSimulationDashboard(Model model, @PathVariable BigInteger id) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "simulation";
    }

    @GetMapping("/simulation/{id}/start")
    public String startSimulation(@PathVariable BigInteger id) {
        simulationService.startSimulation(projectService.getProjectById(id));
        return "redirect:./";
    }

    @GetMapping("/simulation/{id}/stop")
    public String stopSimulation(@PathVariable BigInteger id) {
        simulationService.stopSimulation(projectService.getProjectById(id));
        return "redirect:./";
    }

    @RequestMapping(value = "/simulation/{id}/unit/{unitId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    UnitEnvironment getUnitEnvironment(@PathVariable BigInteger id, @PathVariable BigInteger unitId) {
        Project project = projectService.getProjectById(id);
        return unitService.getUnitById(project, unitId).getEnvironment();
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setSimulationService(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Autowired
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }
}
