package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.repository.ProjectRepository;
import it.unicam.cs.mpgc.jtime119159.repository.TaskRepository;
import lombok.Getter;

/**
 * Singleton per l'accesso ai servizi dell'applicazione.
 * Gestisce l'inizializzazione e la fornitura delle istanze dei service.
 */
public class ServiceLocator {
    @Getter
    private static final ServiceLocator instance = new ServiceLocator();

    @Getter
    private final ProjectService projectService;
    @Getter
    private final TaskService taskService;
    @Getter
    private final PlanningService planningService;

    private ServiceLocator() {
        // Inizializzazione Repository
        ProjectRepository projectRepo = new ProjectRepository();
        TaskRepository taskRepo = new TaskRepository();

        // Inizializzazione Servizi
        this.projectService = new ProjectService(projectRepo);
        this.taskService = new TaskService(taskRepo, projectRepo);
        this.planningService = new PlanningService(taskRepo);
    }
}
