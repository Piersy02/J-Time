package it.unicam.cs.mpgc.jtime119159.view;

import it.unicam.cs.mpgc.jtime119159.model.Project;
import it.unicam.cs.mpgc.jtime119159.model.Task;
import it.unicam.cs.mpgc.jtime119159.service.ServiceLocator;
import it.unicam.cs.mpgc.jtime119159.service.report.ProjectSummaryReport;
import it.unicam.cs.mpgc.jtime119159.service.report.ReportStrategy;
import it.unicam.cs.mpgc.jtime119159.service.exception.BusinessException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MainController {

    // --- Componenti UI: Tab Progetti ---
    @FXML private ListView<Project> projectListView;
    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> colTitle, colPriority, colStatus;
    @FXML private TableColumn<Task, Double> colEstimated, colActual;

    // --- Componenti UI: Tab Pianificazione ---
    @FXML private DatePicker planningDatePicker;
    @FXML private Label effortLabel;
    @FXML private TableView<Task> planningTableView;
    @FXML private TableColumn<Task, String> colPlanTitle, colPlanProject;
    @FXML private TableColumn<Task, Double> colPlanTime;

    private final ObservableList<Project> projectData = FXCollections.observableArrayList();
    private final ObservableList<Task> taskData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadProjects();

        // Listener per cambiare i task visualizzati quando si seleziona un progetto
        projectListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) loadTasks(newVal);
        });

        // Formattazione della lista progetti (Nome + Stato)
        projectListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName() + " (" + item.getStatus().getDisplayName() + ")");
            }
        });
    }

    private void setupTableColumns() {
        // Tabella Task Progetto
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colPriority.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPriority().getDisplayName()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().getDisplayName()));
        colEstimated.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
        colActual.setCellValueFactory(new PropertyValueFactory<>("actualTime"));

        // Tabella Pianificazione
        colPlanTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colPlanProject.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProject().getName()));
        colPlanTime.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
    }

    // --- Gestione Progetti ---

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
        dialog.setContentText("Nome:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.isBlank()) {
                ServiceLocator.getInstance().getProjectService().createProject(name);
                loadProjects();
            }
        });
    }

    @FXML
    private void handleCloseProject() {
        Project selected = projectListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            ServiceLocator.getInstance().getProjectService().closeProject(selected);
            loadProjects();
            showAlert(Alert.AlertType.INFORMATION, "Successo", "Progetto chiuso correttamente.");
        } catch (BusinessException e) {
            showAlert(Alert.AlertType.WARNING, "Vincolo Violato", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProject() {
        Project selected = projectListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Eliminare il progetto '" + selected.getName() + "' e tutti i suoi task?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ServiceLocator.getInstance().getProjectService().deleteProject(selected);
                loadProjects();
                taskData.clear(); // Pulisce la tabella task
            }
        });
    }

    @FXML
    private void handleReopenProject() {
        Project selected = projectListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        ServiceLocator.getInstance().getProjectService().reopenProject(selected);
        loadProjects();
    }

    // --- Gestione Task ---

    @FXML
    private void handleNewTask() {
        Project selected = projectListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona prima un progetto dalla lista.");
            return;
        }

        try {
            // 1. Carichiamo l'FXML (che ora ha DialogPane come root)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task_dialog.fxml"));
            DialogPane dialogPane = loader.load();

            // 2. Otteniamo il controller associato all'FXML
            TaskDialogController controller = loader.getController();

            // 3. Creiamo il Dialog di JavaFX
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Aggiungi Nuova Attività");

            // Aggiungiamo i bottoni standard OK e ANNULLA
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 4. Mostriamo il dialogo e attendiamo la risposta
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Task newTask = controller.getTask(); // Recupera i dati dal controller del dialogo
                if (newTask != null) {
                    // Salviamo tramite il service
                    ServiceLocator.getInstance().getTaskService().addTask(selected, newTask);

                    // Aggiorniamo la tabella
                    loadTasks(selected);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Dati non validi. Assicurati che il tempo sia un numero.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore Critico", "Impossibile caricare la finestra di dialogo.");
        }
    }

    @FXML
    private void handleCompleteTask() {
        Task selected = taskTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog("0.0");
        dialog.setTitle("Completa Attività");
        dialog.setHeaderText("Inserisci durata effettiva per: " + selected.getTitle());
        dialog.setContentText("Ore:");

        dialog.showAndWait().ifPresent(val -> {
            try {
                double actual = Double.parseDouble(val);
                ServiceLocator.getInstance().getTaskService().completeTask(selected, actual);
                loadTasks(projectListView.getSelectionModel().getSelectedItem());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Errore", "Inserire un numero valido.");
            }
        });
    }

    // --- Pianificazione e Report ---

    @FXML
    private void handleLoadPlanning() {
        LocalDate date = planningDatePicker.getValue();
        if (date == null) return;

        List<Task> tasks = ServiceLocator.getInstance().getPlanningService().getTasksForDay(date);
        double total = ServiceLocator.getInstance().getPlanningService().getTotalEffortForDay(date);

        effortLabel.setText("Impegno totale per il giorno: " + total + " ore");
        // Segnalazione visiva se si superano le 8 ore (carico eccessivo)
        effortLabel.setStyle(total > 8.0 ? "-fx-text-fill: red; -fx-font-weight: bold;" : "-fx-text-fill: black;");

        planningTableView.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    private void handleProjectReport() {
        Project selected = projectListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        ReportStrategy strategy = new ProjectSummaryReport();
        String report = strategy.generateReport(selected.getTasks());

        TextArea textArea = new TextArea(report);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Progetto");
        alert.setHeaderText("Riepilogo attività per: " + selected.getName());
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    @FXML private void handleExit() { System.exit(0); }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}