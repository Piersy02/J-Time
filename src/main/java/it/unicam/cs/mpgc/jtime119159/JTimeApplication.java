package it.unicam.cs.mpgc.jtime119159;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import org.h2.tools.Server;
import java.sql.SQLException;

/**
 * Classe principale dell'applicazione JTime.
 * Avvia l'interfaccia JavaFX.
 */
public class JTimeApplication extends Application {

    private Server webServer;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Avvia il server web H2 sulla porta 8082 (o altra disponibile)
            webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Web Server started at " + webServer.getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to start H2 Web Server.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(JTimeApplication.class.getResource("/fxml/main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("JTime - Gestione Attività");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (webServer != null) {
            webServer.stop();
            System.out.println("H2 Web Server stopped.");
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}