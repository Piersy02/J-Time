## Guida all'Avvio

### Requisiti di Sistema
*   **Java JDK**: Versione 21 o superiore.
*   **Gradle**: Non è necessaria l'installazione, il progetto include il wrapper (`gradlew`).

### Istruzioni per l'Esecuzione

1.  **Scompattare l'archivio**: Estrarre il contenuto dell'archivio `.zip` in una cartella locale.
2.  **Aprire il terminale**: Navigare all'interno della cartella del progetto estratta (dove si trova il file `gradlew` e `build.gradle`).
3.  **Compilare ed Eseguire**:
    *   Eseguire il comando:
        ```cmd
        ./gradlew run
        ```

### Accesso al Database H2

L'applicazione avvia automaticamente un server web H2 per gestire il database.

1.  **Avviare l'applicazione**: Assicurarsi che l'applicazione sia in esecuzione (tramite `./gradlew run` o IDE).
2.  **Aprire il Browser**: Andare all'indirizzo `http://localhost:8082`.
3.  **Inserire le Credenziali**:
    *   **Driver Class**: `org.h2.Driver`
    *   **JDBC URL**: `jdbc:h2:./data/jtime_db`
    *   **User Name**: `admin`
    *   **Password**: `admin`
4.  **Connettersi**: Cliccare su "Connect" o "Test Connection".
