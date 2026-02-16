package it.unicam.cs.mpgc.jtime119159.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe di utilità per la gestione della SessionFactory di Hibernate.
 * Gestisce la configurazione e l'accesso al database.
 */
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Costruisce la SessionFactory caricando la configurazione e creando la
     * directory per il DB se necessario.
     *
     * @return la SessionFactory configurata
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Crea la cartella 'data' se non esiste
            Files.createDirectories(Paths.get("data"));

            return new Configuration().configure().buildSessionFactory();
        } catch (IOException e) {
            System.err.println("Errore nella creazione della cartella dati: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
