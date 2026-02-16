package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servizio per la pianificazione delle attività.
 * Gestisce il calcolo del carico di lavoro e il recupero dei task per data.
 */
@RequiredArgsConstructor
public class PlanningService {
    private final Repository<Task, Long> taskRepository;

    /**
     * Recupera la lista dei task pianificati per una specifica data.
     *
     * @param date la data di interesse
     * @return la lista dei task pianificati per quella data
     */
    public List<Task> getTasksForDay(LocalDate date) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getPlannedDate() != null && t.getPlannedDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Calcola l'impegno totale richiesto per un giorno.
     * Somma le ore stimate di tutti i task pianificati per la data specificata.
     *
     * @param date la data di riferimento
     * @return il totale delle ore stimate
     */
    public double getTotalEffortForDay(LocalDate date) {
        return getTasksForDay(date).stream()
                .mapToDouble(Task::getEstimatedTime)
                .sum();
    }

    /**
     * Verifica se il carico di lavoro supera una soglia (es. 8 ore).
     *
     * @param date  la data da verificare
     * @param limit il limite di ore
     * @return true se il carico supera il limite, false altrimenti
     */
    public boolean isOverloaded(LocalDate date, double limit) {
        return getTotalEffortForDay(date) > limit;
    }
}