package it.unicam.cs.mpgc.jtime119159.repository;

import it.unicam.cs.mpgc.jtime119159.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

public abstract class AbstractHibernateRepository<T, ID> implements Repository<T, ID> {

    private final Class<T> entityClass;

    protected AbstractHibernateRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
        executeInTransaction(session -> session.persist(entity));
    }

    @Override
    public void update(T entity) {
        executeInTransaction(session -> session.merge(entity));
    }

    @Override
    public void delete(T entity) {
        executeInTransaction(session -> session.remove(session.contains(entity) ? entity : session.merge(entity)));
    }

    @Override
    public Optional<T> findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        }
    }

    // Metodo helper per gestire le transazioni in modo sicuro
    protected void executeInTransaction(java.util.function.Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Errore durante la transazione DB", e);
        }
    }
}
