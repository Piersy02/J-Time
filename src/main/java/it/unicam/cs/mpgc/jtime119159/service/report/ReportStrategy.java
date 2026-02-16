package it.unicam.cs.mpgc.jtime119159.service.report;

import it.unicam.cs.mpgc.jtime119159.model.Task;

import java.util.List;

/**
 * Interfaccia per la generazione di report (Pattern Strategy).
 */
public interface ReportStrategy {
    String generateReport(List<Task> tasks);
}
