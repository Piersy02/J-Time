package it.unicam.cs.mpgc.jtime119159.model;

import it.unicam.cs.mpgc.jtime119159.enums.Priority;
import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double estimatedTime; // In ore
    private double actualTime;    // In ore (inserita a fine attività)
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
}
