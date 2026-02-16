package it.unicam.cs.mpgc.jtime119159.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entità che rappresenta un tag assegnabile alle attività.
 * Utilizzato per categorizzare e filtrare i task.
 */
@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    @ToString.Exclude // Evita ricorsione
    private Set<Task> tasks = new HashSet<>();
}
