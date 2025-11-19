package application;

import java.util.logging.Logger;

import UI.MenuContratacion;
import UI.SplashScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class RecitalApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
    	
    	final Logger logger = Logger.getLogger(RecitalApplication.class.getName());
    	
    	// Se setean dimensiones de la interfaz gráfica
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        SplashScreen splash = new SplashScreen(() -> {
            var menuContratacion = new MenuContratacion();
            primaryStage.getScene().setRoot(menuContratacion);
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