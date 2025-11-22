package application;

import java.util.logging.Logger;

import UI.MenuContratacion;
import UI.SplashScreen;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class RecitalApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
    	
    	final Logger logger = Logger.getLogger(RecitalApplication.class.getName());
    	
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        SplashScreen splash = new SplashScreen(() -> {
            Platform.runLater(() -> {
                try {
                    Dialog<ButtonType> dlg = new Dialog<>();
                    dlg.setTitle("Cargar archivos de datos (opcional)");
                    dlg.setHeaderText("Ingrese los paths de los archivos a cargar o deje los valores por defecto");

                    ButtonType ok = new ButtonType("Aceptar", ButtonType.OK.getButtonData());
                    dlg.getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField txtRecital = new TextField(System.getProperty("recital.data.file", "Data/recital.json"));
                    TextField txtArtistas = new TextField(System.getProperty("artistas.data.file", "Data/artistas.json"));
                    TextField txtCanciones = new TextField(System.getProperty("canciones.data.file", "Data/canciones.json"));

                    grid.add(new javafx.scene.control.Label("Recital JSON:"), 0, 0);
                    grid.add(txtRecital, 1, 0);
                    grid.add(new javafx.scene.control.Label("Artistas JSON:"), 0, 1);
                    grid.add(txtArtistas, 1, 1);
                    grid.add(new javafx.scene.control.Label("Canciones JSON:"), 0, 2);
                    grid.add(txtCanciones, 1, 2);

                    dlg.getDialogPane().setContent(grid);

                    dlg.showAndWait().ifPresent(bt -> {
                        if (bt == ok) {
                            String r = txtRecital.getText();
                            String a = txtArtistas.getText();
                            String c = txtCanciones.getText();

                            if (r != null && !r.isBlank()) System.setProperty("recital.data.file", r.trim());
                            if (a != null && !a.isBlank()) System.setProperty("artistas.data.file", a.trim());
                            if (c != null && !c.isBlank()) System.setProperty("canciones.data.file", c.trim());
                        }
                    });
                } catch (Exception ex) {
                }

                var menuContratacion = new MenuContratacion();
                primaryStage.getScene().setRoot(menuContratacion);
            });
        });
        
        Scene scene = new Scene(splash.getRoot(), screenBounds.getWidth(), screenBounds.getHeight());
        
        logger.info("Iniciando Sistema de Armado de Recital");
        
        primaryStage.setTitle("🎵 Sistema de Armado de Recital - Paradigmas de Programación - UNLaM");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
      
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());
        
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(650);
        
        primaryStage.show();
        
        splash.iniciarAnimaciones();
    }

    public static void main(String[] args) {
        launch(args);
    }
}