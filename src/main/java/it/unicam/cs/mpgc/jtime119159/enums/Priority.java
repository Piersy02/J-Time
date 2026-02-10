package it.unicam.cs.mpgc.jtime119159.enums;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("Bassa"),
    MEDIUM("Media"),
    HIGH("Alta"),
    CRITICAL("Critica");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }
}
