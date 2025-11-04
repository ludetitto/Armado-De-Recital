package application;

import UI.MenuContratacion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecitalApplication extends Application {

    @Override
    public void start(Stage stage) {
        var root = new MenuContratacion();
        var scene = new Scene(root, 1000, 650);
        stage.setTitle("Armado de Recital - UNLaM");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
