package UI;

import controllers.*;
import domain.Recital;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private final CancionService cancionService = new CancionService();
    private final RecitalService recitalService = new RecitalService();
    private final ComandoHistorial historial = new ComandoHistorial();
    private final Label statusLabel = new Label("Sistema listo");

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MenuContratacion() {
        getStyleClass().add("root-pane");
        
        // Header con título y logo
        VBox header = crearHeader();
        setTop(header);

        // Panel central con grid de acciones y consola
        VBox centerContent = new VBox(15);
        centerContent.setPadding(new Insets(20));
        
        // Grid de botones organizados por categorías
        VBox botonesPanel = crearPanelBotones();
        
        // Consola estilizada
        VBox consolaPanel = crearPanelConsola();
        
        centerContent.getChildren().addAll(botonesPanel, consolaPanel);
        setCenter(centerContent);

        // Footer con estado
        HBox footer = crearFooter();
        setBottom(footer);

        inicializarDatos();
        aplicarEstilos();
        
        refreshConsola("🎸 Sistema Iniciado", 
                "Bienvenido al Sistema de Gestión de Recitales\n" +
                "Recital: " + Recital.getInstance().getTitulo() + "\n\n" +
                "Utiliza los botones superiores para gestionar artistas, canciones y contrataciones.", 
                null);
    }

    private VBox crearHeader() {
        VBox header = new VBox(10);
        header.getStyleClass().add("header");
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("🎵Sistema de Armado de Recital");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 28));
        titulo.getStyleClass().add("titulo-principal");

        Label subtitulo = new Label("Administración y Contratación de Artistas - UNLaM");
        subtitulo.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitulo.getStyleClass().add("subtitulo");

        header.getChildren().addAll(titulo, subtitulo);
        return header;
    }

    private VBox crearPanelBotones() {
        VBox panel = new VBox(15);
        
        // Sección: Consultas
        Label lblConsultas = new Label("📊 CONSULTAS");
        lblConsultas.getStyleClass().add("seccion-titulo");
        
        HBox consultasBox = new HBox(10);
        consultasBox.setAlignment(Pos.CENTER_LEFT);
        
        Button btnRolesCancion = crearBotonAccion("🎤 Roles Faltantes\n(Canción)", "btn-consulta");
        Button btnRolesRecital = crearBotonAccion("🎸 Roles Faltantes\n(Recital)", "btn-consulta");
        Button btnListarArtistas = crearBotonAccion("👥 Listar\nArtistas", "btn-info");
        Button btnListarCanciones = crearBotonAccion("🎵 Listar\nCanciones", "btn-info");
        
        btnRolesCancion.setOnAction(e -> opcionRolesFaltantesCancion());
        btnRolesRecital.setOnAction(e -> opcionRolesFaltantesRecital());
        btnListarArtistas.setOnAction(e -> opcionListarContratados());
        btnListarCanciones.setOnAction(e -> opcionListarCanciones());
        
        consultasBox.getChildren().addAll(btnRolesCancion, btnRolesRecital, btnListarArtistas, btnListarCanciones);

        // Sección: Acciones
        Label lblAcciones = new Label("⚡ ACCIONES");
        lblAcciones.getStyleClass().add("seccion-titulo");
        
        HBox accionesBox = new HBox(10);
        accionesBox.setAlignment(Pos.CENTER_LEFT);
        
        Button btnContratarCancion = crearBotonAccion("✅ Contratar\n(Canción)", "btn-accion");
        Button btnContratarRecital = crearBotonAccion("✅ Contratar\n(Recital)", "btn-accion");
        Button btnEntrenar = crearBotonAccion("💪 Entrenar\nArtista", "btn-entrenar");
        Button btnConsultaProlog = crearBotonAccion("🔍 Consultas\nProlog", "btn-especial");
        
        btnContratarCancion.setOnAction(e -> opcionContratarCancion());
        btnContratarRecital.setOnAction(e -> opcionContratarRecital());
        btnEntrenar.setOnAction(e -> opcionEntrenarArtista());
        btnConsultaProlog.setOnAction(e -> opcionConsultaProlog());
        
        accionesBox.getChildren().addAll(btnContratarCancion, btnContratarRecital, btnEntrenar, btnConsultaProlog);

        panel.getChildren().addAll(lblConsultas, consultasBox, lblAcciones, accionesBox);
        return panel;
    }

    private Button crearBotonAccion(String texto, String styleClass) {
        Button btn = new Button(texto);
        btn.getStyleClass().addAll("boton-accion", styleClass);
        btn.setPrefSize(140, 70);
        btn.setAlignment(Pos.CENTER);
        return btn;
    }

    private VBox crearPanelConsola() {
        VBox panel = new VBox(10);
        
        Label lblConsola = new Label("📋 CONSOLA DE RESULTADOS");
        lblConsola.getStyleClass().add("seccion-titulo");
        
        consola.setEditable(false);
        consola.setWrapText(true);
        consola.getStyleClass().add("consola");
        consola.setPrefHeight(300);
        
        panel.getChildren().addAll(lblConsola, consola);
        VBox.setVgrow(consola, Priority.ALWAYS);
        return panel;
    }

    private HBox crearFooter() {
        HBox footer = new HBox(20);
        footer.getStyleClass().add("footer");
        footer.setPadding(new Insets(15, 30, 15, 30));
        footer.setAlignment(Pos.CENTER_LEFT);

        statusLabel.getStyleClass().add("status-label");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label copyright = new Label("© 2024 Sistema de Recitales - UNLaM");
        copyright.getStyleClass().add("copyright");

        footer.getChildren().addAll(statusLabel, spacer, copyright);
        return footer;
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
            actualizarStatus("✅ Datos cargados correctamente");
        } catch (Exception ex) {
            refreshConsola("❌ Error", "Error inicializando datos: " + ex.getMessage(), null);
            actualizarStatus("⚠️ Error al cargar datos");
        }
    }

    private void aplicarEstilos() {
        String css = 
            ".root-pane {" +
            "    -fx-background-color: linear-gradient(to bottom, #0f0c29, #302b63, #24243e);" +
            "}" +
            ".header {" +
            "    -fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);" +
            "    -fx-background-radius: 10;" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);" +
            "}" +
            ".titulo-principal {" +
            "    -fx-text-fill: white;" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);" +
            "}" +
            ".subtitulo {" +
            "    -fx-text-fill: rgba(255,255,255,0.9);" +
            "}" +
            ".seccion-titulo {" +
            "    -fx-text-fill: #a8b2ff;" +
            "    -fx-font-size: 16px;" +
            "    -fx-font-weight: bold;" +
            "    -fx-padding: 10 0 5 0;" +
            "}" +
            ".boton-accion {" +
            "    -fx-background-radius: 12;" +
            "    -fx-text-fill: white;" +
            "    -fx-font-size: 13px;" +
            "    -fx-font-weight: bold;" +
            "    -fx-cursor: hand;" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);" +
            "}" +
            ".boton-accion:hover {" +
            "    -fx-scale-x: 1.05;" +
            "    -fx-scale-y: 1.05;" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);" +
            "}" +
            ".btn-consulta {" +
            "    -fx-background-color: linear-gradient(to bottom, #4facfe 0%, #00f2fe 100%);" +
            "}" +
            ".btn-info {" +
            "    -fx-background-color: linear-gradient(to bottom, #43e97b 0%, #38f9d7 100%);" +
            "}" +
            ".btn-accion {" +
            "    -fx-background-color: linear-gradient(to bottom, #fa709a 0%, #fee140 100%);" +
            "}" +
            ".btn-entrenar {" +
            "    -fx-background-color: linear-gradient(to bottom, #ff6a00 0%, #ee0979 100%);" +
            "}" +
            ".btn-especial {" +
            "    -fx-background-color: linear-gradient(to bottom, #a8edea 0%, #fed6e3 100%);" +
            "    -fx-text-fill: #333;" +
            "}" +
            ".consola {" +
            "    -fx-background-color: #1a1a2e;" +
            "    -fx-text-fill: #00ff00;" +
            "    -fx-font-family: 'Consolas', 'Monaco', monospace;" +
            "    -fx-font-size: 13px;" +
            "    -fx-background-radius: 8;" +
            "    -fx-border-color: #667eea;" +
            "    -fx-border-width: 2;" +
            "    -fx-border-radius: 8;" +
            "    -fx-padding: 10;" +
            "}" +
            ".footer {" +
            "    -fx-background-color: rgba(0,0,0,0.3);" +
            "    -fx-background-radius: 10;" +
            "}" +
            ".status-label {" +
            "    -fx-text-fill: #4ade80;" +
            "    -fx-font-weight: bold;" +
            "}" +
            ".copyright {" +
            "    -fx-text-fill: rgba(255,255,255,0.6);" +
            "    -fx-font-size: 11px;" +
            "}";
        
        this.setStyle(css);
    }

    private void actualizarStatus(String mensaje) {
        statusLabel.setText(mensaje);
    }

    // === Acciones (todas refrescan consola) ===

    private void opcionRolesFaltantesCancion() {
        TextInputDialog dlg = crearDialogoEstilizado("🎤 Roles Faltantes por Canción", 
                                                      "Ingrese el título exacto de la canción");
        dlg.showAndWait().ifPresent(titulo -> {
            actualizarStatus("🔍 Consultando roles para: " + titulo);
            var cmd = new ListarRolesFaltantesCancionCommand(cancionService, titulo);
            String out = runAndCapture(cmd::ejecutar);
            refreshConsola("🎤 Roles faltantes - Canción: " + titulo, out, null);
            actualizarStatus("✅ Consulta completada");
        });
    }

    private void opcionRolesFaltantesRecital() {
        actualizarStatus("🔍 Consultando roles del recital...");
        var cmd = new ListarRolesFaltantesRecitalCommand(recitalService);
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("🎸 Roles faltantes - Recital", out, null);
        actualizarStatus("✅ Consulta completada");
    }

    private void opcionContratarCancion() {
        TextInputDialog dlg = crearDialogoEstilizado("✅ Contratar Artistas para Canción", 
                                                      "Ingrese el título de la canción");
        dlg.showAndWait().ifPresent(titulo -> {
            actualizarStatus("💼 Contratando artistas para: " + titulo);
            var cmd = new ContratarArtistasParaCancionCommand(titulo);
            String out = runAndCapture(cmd::ejecutar);
            var listar = new ListarRolesFaltantesCancionCommand(cancionService, titulo);
            String estado = runAndCapture(listar::ejecutar);
            refreshConsola("✅ Contratación - Canción: " + titulo, out + "\n" + estado, null);
            actualizarStatus("✅ Contratación completada");
        });
    }

    private void opcionContratarRecital() {
        actualizarStatus("💼 Contratando artistas para el recital...");
        var cmd = new ContratarArtistasParaRecitalCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("✅ Contratación - Recital completo", out, null);
        actualizarStatus("✅ Contratación completada");
    }

    private void opcionEntrenarArtista() {
        TextInputDialog dlg = crearDialogoEstilizado("💪 Entrenar Artista", 
                                                      "Ingrese el nombre del artista");
        dlg.showAndWait().ifPresent(nombre -> {
            actualizarStatus("💪 Entrenando a: " + nombre);
            var cmd = new EntrenarArtistaCommand(nombre);
            String out = runAndCapture(cmd::ejecutar);
            refreshConsola("💪 Entrenamiento - " + nombre, out, null);
            actualizarStatus("✅ Entrenamiento completado");
        });
    }

    private void opcionListarContratados() {
        actualizarStatus("📋 Listando artistas...");
        var cmd = new ListarArtistasCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("👥 Artistas del Recital", out, null);
        actualizarStatus("✅ Lista generada");
    }

    private void opcionListarCanciones() {
        actualizarStatus("📋 Listando canciones...");
        var cmd = new ListarCancionesCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("🎵 Canciones del Recital", out, null);
        actualizarStatus("✅ Lista generada");
    }

    private void opcionConsultaProlog() {
        TextInputDialog dlg = crearDialogoEstilizado("🔍 Consulta Prolog", 
                                                      "Ingrese una consulta (ej: guitarrista(X).)");
        dlg.getEditor().setText("guitarrista(X).");
        dlg.showAndWait().ifPresent(q -> {
            actualizarStatus("🔍 Ejecutando consulta Prolog...");
            String out = runAndCapture(() -> {
                System.out.println("[Consulta Prolog] " + q);
            });
            refreshConsola("🔍 Consulta Prolog", out, null);
            actualizarStatus("✅ Consulta ejecutada");
        });
    }

    private TextInputDialog crearDialogoEstilizado(String titulo, String mensaje) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle(titulo);
        dlg.setHeaderText(mensaje);
        dlg.setContentText("Entrada:");
        return dlg;
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

    private void refreshConsola(String titulo, String body, String extraPie) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║  ").append(titulo).append("\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        if (body != null && !body.isBlank()) {
            sb.append(body.trim()).append("\n");
        } else {
            sb.append("(sin resultados)\n");
        }
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append("║  ⏰ ").append(LocalDateTime.now().format(TS)).append("\n");
        if (extraPie != null && !extraPie.isBlank()) {
            sb.append("║  ").append(extraPie).append("\n");
        }
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");

        String texto = sb.toString();
        historial.agregar(texto);

        consola.clear();
        consola.setText(texto);
        consola.positionCaret(consola.getText().length());
    }
}