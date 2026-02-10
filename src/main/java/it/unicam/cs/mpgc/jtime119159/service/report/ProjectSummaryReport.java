package it.unicam.cs.mpgc.jtime119159.service.report;

import it.unicam.cs.mpgc.jtime119159.model.Task;

import java.util.List;

public class ProjectSummaryReport implements ReportStrategy {

    @Override
    public String generateReport(List<Task> tasks) {
        StringBuilder sb = new StringBuilder("--- Riepilogo Attività ---\n");
        for (Task t : tasks) {
            String status = t.isCompleted() ? "[FATTO]" : "[PENDENTE]";
            sb.append(String.format("%s %s: Stimato %.1fh, Effettivo %.1fh\n",
                    status, t.getTitle(), t.getEstimatedTime(), t.getActualTime()));
        }
        return sb.toString();
    }
}