package it.unicam.cs.mpgc.jtime119159.model;

import it.unicam.cs.mpgc.jtime119159.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entità che rappresenta un progetto nel sistema.
 * Un progetto contiene una lista di attività (Task) e ha uno stato.
 */
@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.ACTIVE; // Default all'apertura

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default // Inizializza la lista anche con il builder
    @ToString.Exclude
    private List<Task> tasks = new ArrayList<>();

    /**
     * Metodo di utilità per aggiungere un task al progetto.
     * Imposta automaticamente il riferimento del progetto nel task.
     *
     * @param task il task da aggiungere
     */
    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }
}
