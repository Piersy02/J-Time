package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaskService {
    private final Repository<Task, Long> taskRepository;
    private final Repository<Project, Long> projectRepository;

    public void addTaskToProject(Project project, String title, double estimatedTime, LocalDate date) {
        Task task = Task.builder()
                .title(title)
                .estimatedTime(estimatedTime)
                .plannedDate(date)
                .completed(false)
                .project(project)
                .build();

        project.addTask(task);
        projectRepository.update(project); // Salva il task grazie al Cascade
    }

    public void completeTask(Task task, double actualTime) {
        task.setActualTime(actualTime);
        task.setCompleted(true);
        taskRepository.update(task);
    }

    public List<Task> getTasksByDate(LocalDate date) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getPlannedDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Calcola l'impegno totale per un giorno specifico (richiesto dalla specifica)
     */
    public double getTotalEffortForDate(LocalDate date) {
        return getTasksByDate(date).stream()
                .mapToDouble(Task::getEstimatedTime)
                .sum();
    }
}
