package application;

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
        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        // Crear el splash screen
        SplashScreen splash = new SplashScreen(() -> {
            // Cuando termina el splash, cambiar al menú principal
            var menuContratacion = new MenuContratacion();
            primaryStage.getScene().setRoot(menuContratacion);
        });
        
        // Crear escena con el splash screen inicial
        Scene scene = new Scene(splash.getRoot(), screenBounds.getWidth(), screenBounds.getHeight());
        
        primaryStage.setTitle("🎵 Sistema de Armado de Recital - Paradigmas de Programación - UNLaM");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        
        // Posicionar en el extremo superior izquierdo (pantalla completa sin maximizar)
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        
        // Establecer el tamaño exacto de la pantalla
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());
        
        // Tamaños mínimos
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(650);
        
        primaryStage.show();
        
        // Iniciar las animaciones del splash
        splash.iniciarAnimaciones();
    }

    public static void main(String[] args) {
        launch(args);
    }
}