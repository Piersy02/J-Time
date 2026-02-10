package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlanningService {
    private final Repository<Task, Long> taskRepository;

    public List<Task> getTasksForDay(LocalDate date) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getPlannedDate() != null && t.getPlannedDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Calcola l'impegno totale richiesto per un giorno.
     */
    public double getTotalEffortForDay(LocalDate date) {
        return getTasksForDay(date).stream()
                .mapToDouble(Task::getEstimatedTime)
                .sum();
    }

    /**
     * Verifica se il carico di lavoro supera una soglia (es. 8 ore).
     */
    public boolean isOverloaded(LocalDate date, double limit) {
        return getTotalEffortForDay(date) > limit;
    }
}