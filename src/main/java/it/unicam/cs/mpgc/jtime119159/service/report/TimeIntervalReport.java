package it.unicam.cs.mpgc.jtime119159.service.report;

import it.unicam.cs.mpgc.jtime119159.model.Task;

import java.util.List;

// Esempio di implementazione per intervallo temporale
public class TimeIntervalReport implements ReportStrategy {
    @Override
    public String generate(List<Task> tasks) {
        StringBuilder sb = new StringBuilder("Report Temporale:\n");
        tasks.forEach(t -> sb.append(t.getTitle()).append(" - ").append(t.getActualTime()).append("h\n"));
        return sb.toString();
    }
}
