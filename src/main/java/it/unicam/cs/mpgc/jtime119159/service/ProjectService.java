package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProjectService {
    // Inietteremo il repository qui
    // private final ProjectRepository projectRepository;

    /**
     * Chiude un progetto solo se non ci sono attività pendenti.
     * @param project il progetto da chiudere
     * @throws IllegalStateException se ci sono task non completati
     */
    public void closeProject(Project project) {
        boolean hasPendingTasks = project.getTasks().stream()
                .anyMatch(task -> !task.isCompleted());

        if (hasPendingTasks) {
            throw new IllegalStateException("Impossibile chiudere il progetto: ci sono attività ancora aperte.");
        }

        project.setClosed(true);
        // projectRepository.update(project);
    }

    /**
     * Calcola la differenza tra tempo stimato ed effettivo per un progetto
     */
    public double getTimeVariance(Project project) {
        double totalEstimated = project.getTasks().stream().mapToDouble(Task::getEstimatedTime).sum();
        double totalActual = project.getTasks().stream().mapToDouble(Task::getActualTime).sum();
        return totalActual - totalEstimated;
    }
}