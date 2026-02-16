package it.unicam.cs.mpgc.jtime119159.model;

import it.unicam.cs.mpgc.jtime119159.enums.Priority;
import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;
    private String description;
    private double estimatedTime; // In ore
    private double actualTime; // In ore (inserita a fine attività)
    private LocalDate plannedDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.TO_DO;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @ToString.Exclude // Evita loop infiniti nel toString di Lombok
    private Project project;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}
