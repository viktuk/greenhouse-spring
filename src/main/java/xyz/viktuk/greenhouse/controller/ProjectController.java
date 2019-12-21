package xyz.viktuk.greenhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.service.ProjectService;

import java.math.BigInteger;

@Controller
public class ProjectController {
    private ProjectService projectService;

    @GetMapping
    public String getAllProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects";
    }

    @PostMapping
    public String createProject(Model model, @RequestParam String name) {
        Project project = new Project();
        project.setName(name);
        projectService.saveProject(project);
        return getAllProjects(model);
    }

    @GetMapping("/delete")
    public String deleteProject(@RequestParam BigInteger id) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            projectService.deleteProject(project);
        }
        return "redirect:/";
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
