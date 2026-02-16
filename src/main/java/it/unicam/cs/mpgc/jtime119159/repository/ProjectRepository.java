package it.unicam.cs.mpgc.jtime119159.repository;

import it.unicam.cs.mpgc.jtime119159.model.Project;

/**
 * Repository specifico per la gestione dei progetti.
 */
public class ProjectRepository extends AbstractHibernateRepository<Project, Long> {
    public ProjectRepository() {
        super(Project.class);
    }

    // Qui potrai aggiungere metodi specifici, es: findActiveProjects()
}
