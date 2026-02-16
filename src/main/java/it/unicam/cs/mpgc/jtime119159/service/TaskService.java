package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import lombok.RequiredArgsConstructor;

/**
 * Servizio per la gestione dei task.
 * Gestisce la creazione, eliminazione e completamento delle attività.
 */
@RequiredArgsConstructor
public class TaskService {
    private final Repository<Task, Long> taskRepository;
    private final Repository<Project, Long> projectRepository;

    /**
     * Aggiunge un nuovo task a un progetto esistente.
     * Aggiorna il progetto nel repository.
     *
     * @param project il progetto a cui aggiungere il task
     * @param task    il task da aggiungere
     */
    public void addTask(Project project, Task task) {
        project.addTask(task);
        projectRepository.update(project);
    }

    /**
     * Elimina un task.
     * Se il task appartiene a un progetto, lo rimuove dalla lista dei task del
     * progetto e aggiorna il progetto.
     * Altrimenti, elimina il task direttamente dal repository dei task.
     *
     * @param task il task da eliminare
     */
    public void deleteTask(Task task) {
        Project project = task.getProject();
        if (project != null) {
            project.getTasks().remove(task);
            projectRepository.update(project);
        } else {
            taskRepository.delete(task);
        }
    }

    /**
     * Completa un'attività registrando la durata effettiva e impostando lo stato a
     * COMPLETED.
     *
     * @param task           il task da completare
     * @param actualDuration la durata effettiva in ore
     */
    public void completeTask(Task task, double actualDuration) {
        task.setActualTime(actualDuration);
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.update(task);
    }
}
