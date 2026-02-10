package it.unicam.cs.mpgc.jtime119159.service.report;

import it.unicam.cs.mpgc.jtime119159.model.Task;

import java.util.List;

public interface ReportStrategy {
    String generateReport(List<Task> tasks);
}
