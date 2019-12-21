package xyz.viktuk.greenhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.entity.CultivationParameter;

import java.math.BigInteger;
import java.util.Date;

@Service
public class CultivationParameterService {
    private ProjectService projectService;

    public CultivationParameter getCultivationParameterById(Project project, BigInteger cultivationParameterId) {
        if (cultivationParameterId == null) {
            return null;
        }
        return project.getCultivationParameters().stream().filter(cultivationParameter -> cultivationParameter.getId().equals(cultivationParameterId)).findFirst().orElse(null);
    }

    public CultivationParameter saveCultivationParameter(Project project, CultivationParameter cultivationParameter) {
        if (getCultivationParameterById(project, cultivationParameter.getId()) == null) {
            cultivationParameter.setCreated(new Date());
            project.getCultivationParameters().add(cultivationParameter);
        }
        projectService.saveProject(project);
        return cultivationParameter;
    }

    public void deleteCultivationParameter(Project project, CultivationParameter cultivationParameter) {
        if (cultivationParameter == null || cultivationParameter.getId() == null) {
            return;
        }
        project.getCultivationParameters().remove(cultivationParameter);
        projectService.saveProject(project);
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
