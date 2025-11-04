package UI;

import controllers.*;
import domain.Recital;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import services.CancionService;
import services.RecitalService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuContratacion extends BorderPane {

    private final TextArea consola = new TextArea();
    private final Button btnRolesCancion = new Button("Roles faltantes (Canción)");
    private final Button btnRolesRecital = new Button("Roles faltantes (Recital)");
    private final Button btnContratarCancion = new Button("Contratar para Canción");
    private final Button btnContratarRecital = new Button("Contratar para Recital");
    private final Button btnEntrenar = new Button("Entrenar artista");
    private final Button btnListarArtistas = new Button("Listar artistas");
    private final Button btnListarCanciones = new Button("Listar canciones");
    private final Button btnConsultaProlog = new Button("Consultas Prolog");

    private final CancionService cancionService = new CancionService();
    private final RecitalService recitalService = new RecitalService();
    private final ComandoHistorial historial = new ComandoHistorial();

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MenuContratacion() {
        setPadding(new Insets(12));

        HBox acciones = new HBox(8,
                btnRolesCancion, btnRolesRecital, btnContratarCancion, btnContratarRecital,
                btnEntrenar, btnListarArtistas, btnListarCanciones, btnConsultaProlog
        );
        acciones.setAlignment(Pos.CENTER_LEFT);
        acciones.setPadding(new Insets(0, 0, 10, 0));
        setTop(acciones);

        consola.setEditable(false);
        consola.setWrapText(true);
        setCenter(consola);

        inicializarDatos();

        btnRolesCancion.setOnAction(e -> opcionRolesFaltantesCancion());
        btnRolesRecital.setOnAction(e -> opcionRolesFaltantesRecital());
        btnContratarCancion.setOnAction(e -> opcionContratarCancion());
        btnContratarRecital.setOnAction(e -> opcionContratarRecital());
        btnEntrenar.setOnAction(e -> opcionEntrenarArtista());
        btnListarArtistas.setOnAction(e -> opcionListarContratados());
        btnListarCanciones.setOnAction(e -> opcionListarCanciones());
        btnConsultaProlog.setOnAction(e -> opcionConsultaProlog());

        // Pantalla inicial “limpia”
        refreshConsola("Inicio",
                "Sistema iniciado. Recital: " + Recital.getInstance().getTitulo(),
                null);
    }

    private void inicializarDatos() {
        try {
            var url = getClass().getResource("/data/recital.json");
            Path ruta = null;
            if (url != null) {
                ruta = Paths.get(url.toURI());
            } else {
                var urlTest = getClass().getResource("/data/recitalTest.json");
                if (urlTest != null) ruta = Paths.get(urlTest.toURI());
            }
            if (ruta == null) {
                ruta = Paths.get("data", "recital.json").toAbsolutePath().normalize();
            }
            FuenteRecital fuente = new JsonFuenteRecital(ruta);
            fuente.cargar();
        } catch (Exception ex) {
            refreshConsola("Error", "Error inicializando datos: " + ex.getMessage(), null);
        }
    }

    // === Acciones (todas refrescan consola) ===

    private void opcionRolesFaltantesCancion() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Roles faltantes por Canción");
        dlg.setHeaderText("Ingrese el título exacto de la canción");
        dlg.setContentText("Título:");
        dlg.showAndWait().ifPresent(titulo -> {
            var cmd = new ListarRolesFaltantesCancionCommand(cancionService, titulo);
            String out = runAndCapture(cmd::ejecutar);
            refreshConsola("Roles faltantes - Canción: " + titulo, out, null);
        });
    }

    private void opcionRolesFaltantesRecital() {
        var cmd = new ListarRolesFaltantesRecitalCommand(recitalService);
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("Roles faltantes - Recital", out, null);
    }

    private void opcionContratarCancion() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Contratar artistas para Canción");
        dlg.setHeaderText("Ingrese el título de la canción a contratar");
        dlg.setContentText("Título:");
        dlg.showAndWait().ifPresent(titulo -> {
            var cmd = new ContratarArtistasParaCancionCommand(titulo);
            String out = runAndCapture(cmd::ejecutar);
            // Después de contratar, mostramos también estado general de esa canción
            var listar = new ListarRolesFaltantesCancionCommand(cancionService, titulo);
            String estado = runAndCapture(listar::ejecutar);
            refreshConsola("Contratación - Canción: " + titulo, out + "\n" + estado, null);
        });
    }

    private void opcionContratarRecital() {
        var cmd = new ContratarArtistasParaRecitalCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("Contratación - Recital completo", out, null);
    }

    private void opcionEntrenarArtista() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Entrenamiento de Artista");
        dlg.setHeaderText("Ingrese el nombre del artista a entrenar");
        dlg.setContentText("Artista:");
        dlg.showAndWait().ifPresent(nombre -> {
            var cmd = new EntrenarArtistaCommand(nombre);
            String out = runAndCapture(cmd::ejecutar);
            refreshConsola("Entrenamiento - " + nombre, out, null);
        });
    }

    private void opcionListarContratados() {
        var cmd = new ListarArtistasCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("Artistas del Recital", out, null);
    }

    private void opcionListarCanciones() {
        var cmd = new ListarCancionesCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("Canciones del Recital", out, null);
    }

    private void opcionConsultaProlog() {
        TextInputDialog dlg = new TextInputDialog("guitarrista(X).");
        dlg.setTitle("Consulta Prolog");
        dlg.setHeaderText("Ingrese una consulta mínima (ej: guitarrista(X).)");
        dlg.setContentText("Consulta:");
        dlg.showAndWait().ifPresent(q -> {
            String out = runAndCapture(() -> {
                System.out.println("[Consulta Prolog] " + q);
                // Si tenés un comando/servicio real, llamalo aquí.
            });
            refreshConsola("Consulta Prolog", out, null);
        });
    }

    // === utilidades ===

    private String runAndCapture(Runnable action) {
        PrintStream original = System.out;
        var baos = new ByteArrayOutputStream();
        try (var ps = new PrintStream(baos)) {
            System.setOut(ps);
            action.run();
        } finally {
            System.setOut(original);
        }
        return baos.toString();
    }

    /**
     * Limpia la consola y muestra un bloque con:
     *  - cabecera (titulo)
     *  - contenido de 'body' (lo que imprime el comando)
     *  - pie con timestamp y, opcionalmente, un extra
     */
    private void refreshConsola(String titulo, String body, String extraPie) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(titulo).append(" ===\n");
        if (body != null && !body.isBlank()) {
            sb.append(body.trim()).append("\n");
        } else {
            sb.append("(sin resultados)\n");
        }
        sb.append("----------------------------------------------\n");
        sb.append("Última actualización: ").append(LocalDateTime.now().format(TS)).append("\n");
        if (extraPie != null && !extraPie.isBlank()) {
            sb.append(extraPie).append("\n");
        }

        String texto = sb.toString();
        historial.agregar(texto);

        consola.clear();
        consola.setText(texto);
        consola.positionCaret(consola.getText().length());
    }
}
