package it.unicam.cs.mpgc.jtime119159.repository;

import it.unicam.cs.mpgc.jtime119159.model.Task;

/**
 * Repository specifico per la gestione dei task.
 */
public class TaskRepository extends AbstractHibernateRepository<Task, Long> {
    public TaskRepository() {
        super(Task.class);
    }
}
