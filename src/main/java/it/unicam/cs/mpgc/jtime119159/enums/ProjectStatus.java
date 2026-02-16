package it.unicam.cs.mpgc.jtime119159.enums;

import lombok.Getter;

/**
 * Enum che rappresenta lo stato di un progetto.
 */
@Getter
public enum ProjectStatus {
    ACTIVE("Attivo"),
    COMPLETED("Completato");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }
}
