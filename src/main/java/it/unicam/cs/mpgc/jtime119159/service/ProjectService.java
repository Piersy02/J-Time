package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.enums.ProjectStatus;
import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import it.unicam.cs.mpgc.jtime119159.service.exception.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProjectService {
    private final Repository<Project, Long> projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public void createProject(String name) {
        Project project = Project.builder().name(name).status(ProjectStatus.ACTIVE).build();
        projectRepository.save(project);
    }

    /**
     * Vincolo: Chiude il progetto solo se non ci sono attività pendenti.
     */
    public void closeProject(Project project) {
        boolean hasPending = project.getTasks().stream()
                .anyMatch(t -> t.getStatus() != TaskStatus.COMPLETED);

        if (hasPending) {
            throw new BusinessException("Impossibile chiudere il progetto: sono presenti attività non completate.");
        }

        project.setStatus(ProjectStatus.COMPLETED);
        projectRepository.update(project);
    }

    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    public void reopenProject(Project project) {
        project.setStatus(ProjectStatus.ACTIVE);
        projectRepository.update(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new BusinessException("Progetto non trovato"));
    }
}