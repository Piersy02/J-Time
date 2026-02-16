package it.unicam.cs.mpgc.jtime119159.enums;

import lombok.Getter;

/**
 * Enum che definisce lo stato di avanzamento di un'attività.
 */
@Getter
public enum TaskStatus {
    TO_DO("Da fare"),
    IN_PROGRESS("In corso"),
    COMPLETED("Completato");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
}
