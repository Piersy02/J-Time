package it.unicam.cs.mpgc.jtime119159.service;

import it.unicam.cs.mpgc.jtime119159.enums.ProjectStatus;
import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.repository.Repository;
import it.unicam.cs.mpgc.jtime119159.service.exception.BusinessException;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Servizio per la gestione dei progetti.
 * Fornisce metodi per creare, chiudere, eliminare, riaprire e recuperare
 * progetti.
 */
@RequiredArgsConstructor
public class ProjectService {
    private final Repository<Project, Long> projectRepository;

    /**
     * Recupera tutti i progetti presenti nel sistema.
     *
     * @return la lista di tutti i progetti
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Crea un nuovo progetto con il nome specificato e stato attivo.
     *
     * @param name il nome del nuovo progetto
     */
    public void createProject(String name) {
        Project project = Project.builder().name(name).status(ProjectStatus.ACTIVE).build();
        projectRepository.save(project);
    }

    /**
     * Chiude un progetto se non ci sono attività pendenti.
     * Vincolo: Chiude il progetto solo se non ci sono avvità non completate.
     *
     * @param project il progetto da chiudere
     * @throws BusinessException se ci sono attività non completate
     */
    public void closeProject(Project project) {
        // Ricarica lo stato corrente del progetto dal DB per verificare i task
        // aggiornati
        Project freshProject = getProjectById(project.getId());

        boolean hasPending = freshProject.getTasks().stream()
                .anyMatch(t -> t.getStatus() != TaskStatus.COMPLETED);

        if (hasPending) {
            throw new BusinessException("Impossibile chiudere il progetto: sono presenti attività non completate.");
        }

        freshProject.setStatus(ProjectStatus.COMPLETED);
        projectRepository.update(freshProject);
    }

    /**
     * Elimina un progetto dal sistema.
     *
     * @param project il progetto da eliminare
     */
    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    /**
     * Riapre un progetto chiuso, impostando lo stato ad ACTIVE.
     *
     * @param project il progetto da riaprire
     */
    public void reopenProject(Project project) {
        project.setStatus(ProjectStatus.ACTIVE);
        projectRepository.update(project);
    }

    /**
     * Cerca un progetto per ID.
     *
     * @param id l'identificativo del progetto
     * @return il progetto trovato
     * @throws BusinessException se il progetto non viene trovato
     */
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new BusinessException("Progetto non trovato"));
    }
}