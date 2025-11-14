package UI;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class SplashScreen {

    private final StackPane root;
    private final Runnable onFinish;
    private VBox content;
    private Label iconoMusical;
    private Label sistemaNombre;
    private Label separador;

    public SplashScreen(Runnable onFinish) {
        this.onFinish = onFinish;
        this.root = crearContenido();
    }

    private StackPane crearContenido() {
        // Contenedor principal con los mismos colores del menú - PANTALLA COMPLETA
        StackPane stackPane = new StackPane();
        stackPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2c3e50 0%, #34495e 100%);"
        );

        // Contenedor de contenido
        content = new VBox(25);
        content.setAlignment(Pos.CENTER);

        // Icono musical grande con color azul del tema
        iconoMusical = new Label("🎵");
        iconoMusical.setFont(Font.font(120));
        iconoMusical.setStyle("-fx-effect: dropshadow(gaussian, rgba(52, 152, 219, 0.6), 25, 0, 0, 5);");
        iconoMusical.setOpacity(0); // Inicia invisible

        // Título principal
        Label titulo = new Label("Bienvenido al");
        titulo.setFont(Font.font("System", FontWeight.LIGHT, 32));
        titulo.setTextFill(Color.rgb(255, 255, 255, 0.9));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 3);");

        // Nombre del sistema con color azul destacado
        sistemaNombre = new Label("Sistema de Armado de Recital");
        sistemaNombre.setFont(Font.font("System", FontWeight.BOLD, 42));
        sistemaNombre.setTextFill(Color.rgb(52, 152, 219)); // Color azul del tema (#3498db)
        sistemaNombre.setStyle(
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0, 0, 4);"
        );
        sistemaNombre.setOpacity(0); // Inicia invisible

        // Separador visual con color dorado del tema
        separador = new Label("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        separador.setFont(Font.font("System", FontWeight.BOLD, 18));
        separador.setTextFill(Color.rgb(243, 156, 18)); // Color dorado del tema (#f39c12)
        separador.setStyle("-fx-opacity: 0;");
        separador.setScaleX(0); // Inicia en escala 0

        // Subtítulo
        Label subtitulo = new Label("Universidad Nacional de La Matanza");
        subtitulo.setFont(Font.font("System", FontWeight.NORMAL, 18));
        subtitulo.setTextFill(Color.rgb(255, 255, 255, 0.85));
        subtitulo.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 2);");

        Label paradigmas = new Label("Paradigmas de Programación");
        paradigmas.setFont(Font.font("System", FontWeight.LIGHT, 16));
        paradigmas.setTextFill(Color.rgb(255, 255, 255, 0.75));

        // Indicador de carga con color verde
        Label cargando = new Label("● Inicializando sistema...");
        cargando.setFont(Font.font("System", FontWeight.NORMAL, 15));
        cargando.setTextFill(Color.rgb(39, 174, 96)); // Color verde (#27ae60)
        cargando.setStyle("-fx-padding: 30 0 0 0;");

        content.getChildren().addAll(
            iconoMusical,
            titulo,
            sistemaNombre,
            separador,
            subtitulo,
            paradigmas,
            cargando
        );

        stackPane.getChildren().add(content);
        return stackPane;
    }

    public StackPane getRoot() {
        return root;
    }

    public void iniciarAnimaciones() {
        aplicarAnimaciones();

        // Cerrar después de 1.7 segundos con animación de desvanecimiento
        PauseTransition pause = new PauseTransition(Duration.seconds(1.7));
        pause.setOnFinished(e -> cerrarConAnimacion());
        pause.play();
    }

    private void aplicarAnimaciones() {
        // Animación de escala para el icono musical
        ScaleTransition scaleIcono = new ScaleTransition(Duration.seconds(0.8), iconoMusical);
        scaleIcono.setFromX(0.3);
        scaleIcono.setFromY(0.3);
        scaleIcono.setToX(1.0);
        scaleIcono.setToY(1.0);
        scaleIcono.setCycleCount(1);

        // Animación de aparición para el icono
        FadeTransition fadeIcono = new FadeTransition(Duration.seconds(0.8), iconoMusical);
        fadeIcono.setFromValue(0.0);
        fadeIcono.setToValue(1.0);

        // Animación de aparición para el título
        FadeTransition fadeTitulo = new FadeTransition(Duration.seconds(1.0), sistemaNombre);
        fadeTitulo.setFromValue(0.0);
        fadeTitulo.setToValue(1.0);
        fadeTitulo.setDelay(Duration.seconds(0.3));

        // Animación de escala para el título
        ScaleTransition scaleTitulo = new ScaleTransition(Duration.seconds(1.0), sistemaNombre);
        scaleTitulo.setFromX(0.8);
        scaleTitulo.setFromY(0.8);
        scaleTitulo.setToX(1.0);
        scaleTitulo.setToY(1.0);
        scaleTitulo.setDelay(Duration.seconds(0.3));

        // Animación para el separador
        FadeTransition fadeSeparador = new FadeTransition(Duration.seconds(0.8), separador);
        fadeSeparador.setFromValue(0.0);
        fadeSeparador.setToValue(0.7);
        fadeSeparador.setDelay(Duration.seconds(0.5));

        ScaleTransition scaleSeparador = new ScaleTransition(Duration.seconds(0.8), separador);
        scaleSeparador.setFromX(0.0);
        scaleSeparador.setToX(1.0);
        scaleSeparador.setDelay(Duration.seconds(0.5));

        // Ejecutar animaciones
        scaleIcono.play();
        fadeIcono.play();
        fadeTitulo.play();
        scaleTitulo.play();
        fadeSeparador.play();
        scaleSeparador.play();
    }

    private void cerrarConAnimacion() {
        // Animación de desvanecimiento de todo el contenido
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            if (onFinish != null) {
                onFinish.run();
            }
        });
        fadeOut.play();
    }
}