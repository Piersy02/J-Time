package it.unicam.cs.mpgc.jtime119159.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean closed;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default // Inizializza la lista anche con il builder
    private List<Task> tasks = new ArrayList<>();

    // Metodo di utilità per aggiungere task
    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }
}
