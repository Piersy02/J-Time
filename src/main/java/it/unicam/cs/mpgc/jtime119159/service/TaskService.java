package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskService {
    private final Repository<Task, Long> taskRepository;
    private final Repository<Project, Long> projectRepository;

    public void addTask(Project project, Task task) {
        project.addTask(task);
        projectRepository.update(project);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    /**
     * Completa l'attività registrando la durata effettiva.
     */
    public void completeTask(Task task, double actualDuration) {
        task.setActualTime(actualDuration);
        task.setStatus(TaskStatus.COMPLETED);
        taskRepository.update(task);
    }
}
