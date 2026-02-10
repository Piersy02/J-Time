package it.unicam.cs.mpgc.jtime119159.view;

import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.service.ServiceLocator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class MainController {

    @FXML private ListView<Project> projectListView;
    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colPriority;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, Double> colEstimated;

    private final ObservableList<Project> projectData = FXCollections.observableArrayList();
    private final ObservableList<Task> taskData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadProjects();

        // Listener: quando seleziono un progetto, carica i suoi task
        projectListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        loadTasks(newSelection);
                    }
                }
        );

        // Personalizziamo come vengono visualizzati i progetti nella lista
        projectListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " [" + item.getStatus().getDisplayName() + "]");
            }
        });
    }

    private void setupTableColumns() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEstimated.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
    }

    private void loadProjects() {
        projectData.setAll(ServiceLocator.getInstance().getProjectService().getAllProjects());
        projectListView.setItems(projectData);
    }

    private void loadTasks(Project project) {
        taskData.setAll(project.getTasks());
        taskTableView.setItems(taskData);
    }

    @FXML
    private void handleNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo Progetto");
        dialog.setHeaderText("Crea un nuovo progetto");
        dialog.setContentText("Nome del progetto:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                ServiceLocator.getInstance().getProjectService().createProject(name);
                loadProjects(); // Ricarica la lista
            }
        });
    }

    @FXML private void handleExit() { System.exit(0); }
}
