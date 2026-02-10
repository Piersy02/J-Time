package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.enums.ProjectStatus;
import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import it.unicam.cs.mpgc.jtime119159.service.exception.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProjectService {
    private final Repository<Project, Long> projectRepository;

    public void createProject(String name) {
        Project project = Project.builder().name(name).closed(false).build();
        projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Regola di Business: Un progetto può essere chiuso solo se non ci sono attività pendenti.
     */
    public void closeProject(Project project) {
        // Controllo se ci sono task che NON sono in stato COMPLETED
        boolean hasUnfinishedTasks = project.getTasks().stream()
                .anyMatch(task -> task.getStatus() != TaskStatus.COMPLETED);

        if (hasUnfinishedTasks) {
            throw new BusinessException("Impossibile completare il progetto: ci sono attività non terminate.");
        }

        project.setStatus(ProjectStatus.COMPLETED);
        projectRepository.update(project);
    }

    public List<Project> getActiveProjects() {
        return getAllProjects().stream()
                .filter(p -> !p.isClosed())
                .collect(Collectors.toList());
    }
}