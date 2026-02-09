package it.unicam.cs.mpgc.jtime119159;

import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;

public class JTimeApplication {
    public static void main(String[] args) {
        System.out.println("--- TEST HIBERNATE ---");

        // 1. Apriamo una sessione
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // 2. Creiamo un progetto di prova
            Project p = Project.builder()
                    .name("Esame MPGC")
                    .closed(false)
                    .build();

            // 3. Creiamo un task e lo associamo
            Task t = Task.builder()
                    .title("Studiare Hibernate")
                    .estimatedTime(4.0)
                    .plannedDate(LocalDate.now())
                    .completed(false)
                    .build();

            p.addTask(t);

            // 4. Salviamo (il cascade salverà anche il task)
            session.persist(p);
            transaction.commit();

            System.out.println("Dati salvati con successo!");

            // 5. Verifichiamo la lettura
            Project retrieved = session.get(Project.class, p.getId());
            System.out.println("Progetto recuperato: " + retrieved.getName());
            System.out.println("Task associati: " + retrieved.getTasks().size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}