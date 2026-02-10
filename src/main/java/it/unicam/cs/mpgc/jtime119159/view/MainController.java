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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MainController {

    // --- Componenti UI: Layout Generale ---
    @FXML private StackPane contentArea;
    @FXML private VBox projectsView;
    // Task Universali rimosso
    @FXML private VBox planningView;
    @FXML private VBox reportsView;

    // --- View 1: Progetti ---
    @FXML private ListView<Project> projectListView;
    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> colTitle, colPriority, colStatus;
    @FXML private TableColumn<Task, Double> colEstimated, colActual;

    // Task Universali rimosso

    // --- View 3: Pianificazione ---
    @FXML private DatePicker planningDatePicker;
    @FXML private Label effortLabel;
    @FXML private TableView<Task> planningTableView;
    @FXML private TableColumn<Task, String> colPlanTitle, colPlanProject;
    @FXML private TableColumn<Task, Double> colPlanTime;

    // --- View 4: Report ---
    @FXML private ComboBox<Project> reportProjectCombo;
    @FXML private TextArea reportTextArea;

    // Dati
    private final ObservableList<Project> projectData = FXCollections.observableArrayList();
    private final ObservableList<Task> taskData = FXCollections.observableArrayList();
    // Task Universali rimosso

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
        
        // Formattazione Combo Box Report
        reportProjectCombo.setCellFactory(lv -> new ListCell<>() {
             @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });
        reportProjectCombo.setButtonCell(new ListCell<>() {
             @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });

        // Default view
        showProjectsView();
    }

    private void setupTableColumns() {
        // Tabella Task Progetto
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colPriority.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPriority().getDisplayName()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().getDisplayName()));
        colEstimated.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
        colActual.setCellValueFactory(new PropertyValueFactory<>("actualTime"));

        // Task Universali rimosso

        // Tabella Pianificazione
        colPlanTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colPlanProject.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProject().getName()));
        colPlanTime.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
    }

    // --- Navigazione ---

    @FXML public void showProjectsView() { switchView(projectsView); }
    
    // Task Universali rimosso
    
    @FXML public void showPlanningView() { switchView(planningView); }
    
    @FXML public void showReportsView() { 
        switchView(reportsView);
        reportProjectCombo.setItems(projectData);
    }

    private void switchView(VBox view) {
        projectsView.setVisible(false);
        planningView.setVisible(false);
        reportsView.setVisible(false);
        
        view.setVisible(true);
        view.toFront();
    }

    // --- Caricamento Dati ---

    private void loadProjects() {
        projectData.setAll(ServiceLocator.getInstance().getProjectService().getAllProjects());
        projectListView.setItems(projectData);
    }

    private void loadTasks(Project project) {
        taskData.setAll(project.getTasks());
        taskTableView.setItems(taskData);
    }
    
    // Task Universali rimosso

    // --- Azioni Utente ---

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
                taskData.clear();
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

    @FXML
    private void handleNewTask() {
        Project selected = projectListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona prima un progetto dalla lista.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task_dialog.fxml"));
            DialogPane dialogPane = loader.load();
            TaskDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Aggiungi Nuova Attività");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Task newTask = controller.getTask();
                if (newTask != null) {
                    ServiceLocator.getInstance().getTaskService().addTask(selected, newTask);
                    loadTasks(selected);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Dati non validi.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore Critico", "Impossibile caricare dialog.");
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
    
    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        Project selectedProject = projectListView.getSelectionModel().getSelectedItem();
        
        if (selectedTask == null) {
            showAlert(Alert.AlertType.WARNING, "Selezione", "Seleziona un task da eliminare.");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler eliminare il task '" + selectedTask.getTitle() + "'?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ServiceLocator.getInstance().getTaskService().deleteTask(selectedTask);
                // Refresh project data from DB to ensure tasks list is updated
                if (selectedProject != null) {
                    Project refreshedProject = ServiceLocator.getInstance().getProjectService().getProjectById(selectedProject.getId());
                    // Update the item in the list view (optional, but good for consistency)
                    int index = projectListView.getItems().indexOf(selectedProject);
                    if (index >= 0) {
                        projectListView.getItems().set(index, refreshedProject);
                        projectListView.getSelectionModel().select(index);
                    }
                    loadTasks(refreshedProject);
                }
            }
        });
    }

    @FXML
    private void handleLoadPlanning() {
        LocalDate date = planningDatePicker.getValue();
        if (date == null) return;

        List<Task> tasks = ServiceLocator.getInstance().getPlanningService().getTasksForDay(date);
        double total = ServiceLocator.getInstance().getPlanningService().getTotalEffortForDay(date);

        effortLabel.setText(total + " ore");
        effortLabel.setStyle(total > 8.0 ? "-fx-text-fill: red; -fx-font-weight: bold;" : "-fx-text-fill: #2c3e50;");
        planningTableView.setItems(FXCollections.observableArrayList(tasks));
    }

    @FXML
    private void handleProjectReport() {
        // Legacy method, kept for compatibility if needed or removed if FXML updated completely
        // In new UI, we use handleGenerateReport
        handleGenerateReport();
    }
    
    @FXML
    private void handleGenerateReport() {
        Project selected = reportProjectCombo.getSelectionModel().getSelectedItem();
        if (selected == null) {
             // Try getting from list view if that was the context, OR warn
             selected = projectListView.getSelectionModel().getSelectedItem();
        }
        
        if (selected == null) {
            reportTextArea.setText("Seleziona un progetto per generare il report.");
            return;
        }

        ReportStrategy strategy = new ProjectSummaryReport();
        String report = strategy.generateReport(selected.getTasks());
        reportTextArea.setText(report);
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