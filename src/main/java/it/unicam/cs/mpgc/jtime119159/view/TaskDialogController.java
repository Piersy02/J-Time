package it.unicam.cs.mpgc.jtime119159.view;

import it.unicam.cs.mpgc.jtime119159.enums.Priority;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.enums.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

/**
 * Controller per la finestra di dialogo di creazione di un nuovo task.
 * Recupera i dati inseriti dall'utente e costruisce l'oggetto Task.
 */
public class TaskDialogController {
    @FXML
    private TextField titleField, estimatedField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<Priority> priorityCombo;
    @FXML
    private DatePicker datePicker;

    @FXML
    public void initialize() {
        priorityCombo.getItems().setAll(Priority.values());
        priorityCombo.setValue(Priority.MEDIUM);
        datePicker.setValue(LocalDate.now());
    }

    public Task getTask() {
        try {
            return Task.builder()
                    .title(titleField.getText())
                    .description(descriptionArea.getText())
                    .estimatedTime(Double.parseDouble(estimatedField.getText()))
                    .priority(priorityCombo.getValue())
                    .plannedDate(datePicker.getValue())
                    .status(TaskStatus.TO_DO)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}