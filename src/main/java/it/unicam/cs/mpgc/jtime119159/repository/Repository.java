package it.unicam.cs.mpgc.jtime119159.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia generica per le operazioni CRUD.
 * Rispetta il principio di Interface Segregation.
 */
public interface Repository<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
}
