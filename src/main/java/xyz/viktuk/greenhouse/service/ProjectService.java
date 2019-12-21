package xyz.viktuk.greenhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.viktuk.greenhouse.entity.Project;
import xyz.viktuk.greenhouse.repository.ProjectRepository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(BigInteger projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public Project saveProject(Project project) {
        if (project.getId() == null) {
            project.setCreated(new Date());
        }
        return projectRepository.save(project);
    }

    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
}
