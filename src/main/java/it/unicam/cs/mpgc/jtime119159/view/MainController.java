package it.unicam.cs.mpgc.jtime119159.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;

public class MainController {

    @FXML private ListView<Project> projectListView;
    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colPriority;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, Double> colEstimated;

    @FXML
    public void initialize() {
        // Qui inizializzeremo i dati caricandoli dai Service
        System.out.println("Controller Inizializzato!");
    }

    @FXML
    private void handleNewProject() {
        System.out.println("Bottone Nuovo Progetto cliccato");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
