package it.unicam.cs.mpgc.jtime119159.repository;

import it.unicam.cs.mpgc.jtime119159.model.Task;

public class TaskRepository extends AbstractHibernateRepository<Task, Long> {
    public TaskRepository() {
        super(Task.class);
    }
}
