package application;

import UI.MenuContratacion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecitalApplication extends Application {

    @Override
    public void start(Stage stage) {
        var root = new MenuContratacion();
        var scene = new Scene(root, 1200, 750);
        stage.setTitle("🎵 Armado de Recital - Paradigmas de Programacion - UNLaM");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}