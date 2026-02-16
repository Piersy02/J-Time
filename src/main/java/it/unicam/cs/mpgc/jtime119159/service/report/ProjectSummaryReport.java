package it.unicam.cs.mpgc.jtime119159.service.report;

import it.unicam.cs.mpgc.jtime119159.model.Task;

import java.util.List;

/**
 * Implementazione della strategia di reportistica che genera un riepilogo
 * testuale del progetto.
 * Calcola e mostra la varianza tra tempo stimato ed effettivo per ogni task.
 */
public class ProjectSummaryReport implements ReportStrategy {

    @Override
    public String generateReport(List<Task> tasks) {
        StringBuilder sb = new StringBuilder("REPORT PROGETTO\n");
        for (Task t : tasks) {
            double variance = t.getActualTime() - t.getEstimatedTime();
            sb.append(String.format("- %s: Stimato %.1fh, Effettivo %.1fh (Diff: %.1fh)\n",
                    t.getTitle(), t.getEstimatedTime(), t.getActualTime(), variance));
        }
        return sb.toString();
    }
}