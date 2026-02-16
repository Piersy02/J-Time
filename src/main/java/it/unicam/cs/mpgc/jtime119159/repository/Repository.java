package it.unicam.cs.mpgc.jtime119159.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia generica per le operazioni CRUD.
 * Rispetta il principio di Interface Segregation.
 */
public interface Repository<T, ID> {
    /**
     * Salva una nuova entità nel database.
     * 
     * @param entity l'entità da salvare
     */
    void save(T entity);

    /**
     * Aggiorna un'entità esistente nel database.
     * 
     * @param entity l'entità da aggiornare
     */
    void update(T entity);

    /**
     * Elimina un'entità dal database.
     * 
     * @param entity l'entità da eliminare
     */
    void delete(T entity);

    /**
     * Cerca un'entità per ID.
     * 
     * @param id l'identificativo dell'entità
     * @return un Optional contenente l'entità se trovata, altrimenti vuoto
     */
    Optional<T> findById(ID id);

    /**
     * Restituisce tutte le entità presenti nel database.
     * 
     * @return una lista di tutte le entità
     */
    List<T> findAll();
}
