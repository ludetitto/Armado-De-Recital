package UI;

import controllers.*;
import domain.Recital;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;
import javafx.util.Duration;
import repository.ArtistaRepository;
import repository.CancionRepository;
import repository.FuenteRecital;
import repository.FuenteCancion;
import repository.JsonFuenteArtista;
import repository.JsonFuenteCancion;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;
import repository.FuenteArtista;
import services.ArtistaService;
import services.CancionService;
import services.PrologService;
import services.RecitalService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuContratacion extends BorderPane {

    private final TextArea consola = new TextArea();
    private CancionService cancionService;
    private final RecitalService recitalService = new RecitalService();
    private ArtistaService artistaService;
    private PrologService prologService;
    private final ComandoHistorial historial = new ComandoHistorial();
    private final Label statusLabel = new Label("⚡ Sistema iniciado correctamente");

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Para resaltar botón seleccionado
    private Button botonSeleccionado = null;

    public MenuContratacion() {
        setStyle("-fx-background-color: #f0f2f5;");
        
        // Panel lateral izquierdo - MÁS ANCHO
        VBox panelLateral = crearPanelLateral();
        setLeft(panelLateral);

        // Área central-derecha
        VBox areaCentral = crearAreaCentral();
        setCenter(areaCentral);

        inicializarDatos();
        
        refreshConsola("🎸 ¡Bienvenido al Sistema!", 
                "╔═══════════════════════════════════════════════════════╗\n" +
                "  Sistema de Gestión y Armado de Recitales\n" +
                "╚═══════════════════════════════════════════════════════╝\n\n" +
                "📋 Recital Actual: " + Recital.getInstance().getTitulo() + "\n\n" +
                "💡 Instrucciones:\n" +
                "   • Use los botones del menú lateral para navegar\n" +
                "   • Los resultados aparecerán en esta área central\n" +
                "   • Todas las operaciones se registran aquí\n\n" +
                "✨ ¡Todo listo para comenzar!", 
                null);
    }

    private VBox crearPanelLateral() {
        VBox panel = new VBox();
        panel.setPrefWidth(320);  // AUMENTADO de 280 a 320
        panel.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2c3e50 0%, #34495e 100%);" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 3, 0);"
        );
        
        // Header del panel lateral
        VBox header = new VBox(8);
        header.setPadding(new Insets(20, 20, 20, 20));  // PADDING REDUCIDO
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1a252f;");
      
                
        // ScrollPane para el menú
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPadding(new Insets(10, 0, 10, 0));  // PADDING REDUCIDO
        
        VBox menuContainer = new VBox(12);  // ESPACIADO REDUCIDO de 20 a 12
        menuContainer.setPadding(new Insets(8, 15, 8, 15));  // PADDING REDUCIDO
        
        // Sección Consultas
        VBox seccionConsultas = crearSeccionMenu(
            "📊 CONSULTAS Y REPORTES",
            new MenuItem[] {
                new MenuItem("🎤", "Roles Faltantes (Canción)", this::opcionRolesFaltantesCancion),
                new MenuItem("🎸", "Roles Faltantes (Recital)", this::opcionRolesFaltantesRecital),
                new MenuItem("👥", "Listar Artistas", this::opcionListarContratados),
                new MenuItem("🎵", "Listar Canciones", this::opcionListarCanciones),
                new MenuItem("🔚", "Reportar y Salir", this::reportarYsalir) // guadar un json de recital y salir
            }
        );
        
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.1);");
        
        // Sección Acciones
        VBox seccionAcciones = crearSeccionMenu(
            "⚡ ACCIONES Y GESTIÓN",
            new MenuItem[] {
                new MenuItem("✅", "Contratar (Canción)", this::opcionContratarCancion),
                new MenuItem("✅", "Contratar (Recital)", this::opcionContratarRecital),
                new MenuItem("💪", "Entrenar Artista", this::opcionEntrenarArtista),
                new MenuItem("🔍", "Consultas Prolog", this::opcionConsultaProlog)
            }
        );
        
        menuContainer.getChildren().addAll(seccionConsultas, sep, seccionAcciones);
        scrollPane.setContent(menuContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // Footer del panel lateral
        VBox footer = new VBox(4);  // ESPACIADO REDUCIDO
        footer.setPadding(new Insets(12));  // PADDING REDUCIDO
        footer.setAlignment(Pos.CENTER);
        footer.setStyle(
            "-fx-background-color: #1a252f;" +
            "-fx-border-color: rgba(255,255,255,0.1);" +
            "-fx-border-width: 1 0 0 0;"
        );
        
        Label footerText = new Label("UNLaM");
        footerText.setFont(Font.font("System", FontWeight.LIGHT, 10));  // TAMAÑO REDUCIDO
        footerText.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-text-alignment: center;");
        footerText.setWrapText(true);
        footerText.setAlignment(Pos.CENTER);
        
        Label paradigmas = new Label("Paradigmas de Programación");
        paradigmas.setFont(Font.font("System", FontWeight.LIGHT, 9));  // TAMAÑO REDUCIDO
        paradigmas.setStyle("-fx-text-fill: rgba(255,255,255,0.5);");
        
        footer.getChildren().addAll(footerText, paradigmas);
        
        panel.getChildren().addAll(header, scrollPane, footer);
        return panel;
    }
    
    private VBox crearSeccionMenu(String titulo, MenuItem[] items) {
        VBox seccion = new VBox(6);  // ESPACIADO REDUCIDO de 8 a 6
        
        Label lblTitulo = new Label(titulo);
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 12));  // TAMAÑO REDUCIDO
        lblTitulo.setStyle("-fx-text-fill: #f39c12; -fx-padding: 0 0 4 8;");  // PADDING REDUCIDO
        
        seccion.getChildren().add(lblTitulo);
        
        for (MenuItem item : items) {
            Button btn = crearBotonMenu(item.icono, item.texto, item.accion);
            seccion.getChildren().add(btn);
        }
        
        return seccion;
    }
    
    private Button crearBotonMenu(String icono, String texto, Runnable accion) {
        HBox contenido = new HBox(12);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setPadding(new Insets(10, 15, 10, 15));  // PADDING REDUCIDO (era 12)
        
        Label lblIcono = new Label(icono);
        lblIcono.setFont(Font.font(18));  // TAMAÑO REDUCIDO de 20 a 18
        lblIcono.setStyle("-fx-text-fill: white;");
        lblIcono.setMinWidth(25);  // ANCHO REDUCIDO
        
        Label lblTexto = new Label(texto);
        lblTexto.setFont(Font.font("System", FontWeight.NORMAL, 13));  // TAMAÑO AUMENTADO de 12 a 13
        lblTexto.setStyle("-fx-text-fill: rgba(255,255,255,0.95);");
        lblTexto.setWrapText(false);  // SIN WRAP para que quede en una línea
        
        contenido.getChildren().addAll(lblIcono, lblTexto);
        
        Button btn = new Button();
        btn.setGraphic(contenido);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(45);  // ALTURA REDUCIDA de 60 a 45
        
        String estiloNormal = 
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0;" +
            "-fx-border-radius: 10;";
        
        String estiloHover = 
            "-fx-background-color: rgba(52, 152, 219, 0.2);" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;";
        
        String estiloSeleccionado = 
            "-fx-background-color: #3498db;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #2980b9;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;";
        
        btn.setStyle(estiloNormal);
        
        btn.setOnMouseEntered(e -> {
            if (botonSeleccionado != btn) {
                btn.setStyle(estiloHover);
            }
        });
        
        btn.setOnMouseExited(e -> {
            if (botonSeleccionado != btn) {
                btn.setStyle(estiloNormal);
            }
        });
        
        btn.setOnAction(e -> {
            // Resetear botón anterior
            if (botonSeleccionado != null) {
                botonSeleccionado.setStyle(estiloNormal);
            }
            // Marcar nuevo botón
            botonSeleccionado = btn;
            btn.setStyle(estiloSeleccionado);
            
            // Ejecutar acción
            accion.run();
        });
        
        return btn;
    }
    
    private VBox crearAreaCentral() {
        VBox area = new VBox(20);
        area.setPadding(new Insets(25, 30, 25, 30));
        VBox.setVgrow(area, Priority.ALWAYS);
        
        // Header superior
        VBox header = new VBox(10);
        header.setPadding(new Insets(25, 30, 25, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        Label titulo = new Label("Sistema de Administración y Contratación de Artistas");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 22));
        titulo.setStyle("-fx-text-fill: #2c3e50;");
        
        Label subtitulo = new Label("Gestión completa de recitales y artistas");
        subtitulo.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitulo.setStyle("-fx-text-fill: #7f8c8d;");
        
        header.getChildren().addAll(titulo, subtitulo);
        
        // Panel de consola
        VBox consolaPanel = crearPanelConsola();
        VBox.setVgrow(consolaPanel, Priority.ALWAYS);
        
        // Footer con status
        HBox footer = crearFooterCentral();
        
        area.getChildren().addAll(header, consolaPanel, footer);
        return area;
    }
    
    private VBox crearPanelConsola() {
        VBox panel = new VBox(0);
        VBox.setVgrow(panel, Priority.ALWAYS);
        
        // Header de la consola
        HBox consolaHeader = new HBox(15);
        consolaHeader.setAlignment(Pos.CENTER_LEFT);
        consolaHeader.setPadding(new Insets(15, 20, 15, 20));
        consolaHeader.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-background-radius: 12 12 0 0;"
        );
        
        Label lblConsola = new Label("📋 Consola de Resultados");
        lblConsola.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblConsola.setStyle("-fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button btnLimpiar = new Button("🗑️ Limpiar");
        btnLimpiar.setStyle(
            "-fx-background-color: rgba(255,255,255,0.3);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16 8 16;"
        );
        btnLimpiar.setOnMouseEntered(e -> 
            btnLimpiar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.4);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 16 8 16;"
            )
        );
        btnLimpiar.setOnMouseExited(e -> 
            btnLimpiar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 16 8 16;"
            )
        );
        btnLimpiar.setOnAction(e -> {
            consola.clear();
            refreshConsola("🎸 Consola Limpiada", "La consola ha sido limpiada correctamente.", null);
        });
        
        consolaHeader.getChildren().addAll(lblConsola, spacer, btnLimpiar);
        
        // Consola
        consola.setEditable(false);
        consola.setWrapText(true);
        consola.setStyle(
            "-fx-control-inner-background: #0d1117;" +
            "-fx-text-fill: #58d68d;" +
            "-fx-font-family: 'Consolas', 'Monaco', 'Courier New', monospace;" +
            "-fx-font-size: 14px;" +
            "-fx-background-color: #0d1117;" +
            "-fx-background-radius: 0 0 12 12;" +
            "-fx-border-color: #3498db;" +
            "-fx-border-width: 0 2 2 2;" +
            "-fx-border-radius: 0 0 12 12;" +
            "-fx-padding: 20;"
        );
        
        VBox.setVgrow(consola, Priority.ALWAYS);
        
        panel.getChildren().addAll(consolaHeader, consola);
        return panel;
    }
    
    private HBox crearFooterCentral() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(15, 20, 15, 20));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );

        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        
        Label statusIcon = new Label("●");
        statusIcon.setFont(Font.font(14));
        statusIcon.setStyle("-fx-text-fill: #27ae60;");
        
        statusLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
        statusLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        statusBox.getChildren().addAll(statusIcon, statusLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label copyright = new Label("© 2024 UNLaM - Paradigmas de Programación");
        copyright.setFont(Font.font("System", FontWeight.LIGHT, 11));
        copyright.setStyle("-fx-text-fill: #95a5a6;");

        footer.getChildren().addAll(statusBox, spacer, copyright);
        return footer;
    }

    private void inicializarDatos() {
    	
    	ArtistaRepository artistaRepository = new ArtistaRepository();
    	CancionRepository cancionRepository = new CancionRepository();
    	
    	RecitalRepository.setRecitalInstance(Recital.getInstance());
    	
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
            FuenteRecital fuente = new JsonFuenteRecital(ruta, artistaRepository,cancionRepository);
            fuente.cargar();
            actualizarStatus("✅ Datos cargados exitosamente");
        } catch (Exception ex) {
            refreshConsola("❌ Error Crítico", "Error al inicializar datos: " + ex.getMessage(), null);
            actualizarStatus("⚠️ Error en la carga de datos");
        }
        
        try {
            var url = getClass().getResource("/data/artistas.json");
            Path ruta = null;
            if (url != null) {
                ruta = Paths.get(url.toURI());
            } else {
                var urlTest = getClass().getResource("/data/artistasTest.json");
                if (urlTest != null) ruta = Paths.get(urlTest.toURI());
            }
            if (ruta == null) {
                ruta = Paths.get("data", "artistas.json").toAbsolutePath().normalize();
            }
            
            
            artistaService = new ArtistaService(artistaRepository);
            prologService = new PrologService(artistaRepository, cancionRepository);
            FuenteArtista fuente = new JsonFuenteArtista(ruta, artistaRepository);
            fuente.cargar();
            
            actualizarStatus("✅ Datos cargados exitosamente");
        } catch (Exception ex) {
            refreshConsola("❌ Error Crítico", "Error al inicializar datos: " + ex.getMessage(), null);
            actualizarStatus("⚠️ Error en la carga de datos");
        }
        
        try {
            var url = getClass().getResource("/data/canciones.json");
            Path ruta = null;
            if (url != null) {
                ruta = Paths.get(url.toURI());
            } else {
                var urlTest = getClass().getResource("/data/cancionesTest.json");
                if (urlTest != null) ruta = Paths.get(urlTest.toURI());
            }
            if (ruta == null) {
                ruta = Paths.get("data", "canciones.json").toAbsolutePath().normalize();
            }
            
            
            cancionService = new CancionService();
            FuenteCancion fuente = new JsonFuenteCancion(ruta, cancionRepository);
            fuente.cargar();
            
            actualizarStatus("✅ Datos cargados exitosamente");
        } catch (Exception ex) {
            refreshConsola("❌ Error Crítico", "Error al inicializar datos: " + ex.getMessage(), null);
            actualizarStatus("⚠️ Error en la carga de datos");
        }
    }

    private void actualizarStatus(String mensaje) {
        statusLabel.setText(mensaje);
    }

    // === Acciones ===

    private void opcionRolesFaltantesCancion() {
        TextInputDialog dlg = crearDialogoEstilizado("🎤 Roles Faltantes por Canción", 
                                                      "Ingrese el título exacto de la canción");
        dlg.showAndWait().ifPresent(titulo -> {
            actualizarStatus("🔍 Analizando roles para: " + titulo);
            var cmd = new ListarRolesFaltantesCancionCommand(cancionService, titulo);
            String out = runAndCapture(cmd::ejecutar);
            refreshConsola("🎤 Análisis de Roles - Canción: " + titulo, out, "Consulta completada exitosamente");
            actualizarStatus("✅ Análisis completado");
        });
    }

    private void opcionRolesFaltantesRecital() {
        actualizarStatus("🔍 Analizando roles del recital completo...");
        var cmd = new ListarRolesFaltantesRecitalCommand(recitalService);
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("🎸 Análisis de Roles - Recital Completo", out, "Análisis global completado");
        actualizarStatus("✅ Análisis completado");
    }

    private void opcionContratarCancion() {
        TextInputDialog dlg = crearDialogoEstilizado("✅ Contratar Artistas para Canción", 
                                                      "Ingrese el título de la canción a contratar");
        dlg.showAndWait().ifPresent(titulo -> {
            actualizarStatus("💼 Procesando contratación para: " + titulo);
            var cmd = new ContratarArtistasParaCancionCommand(artistaService, cancionService, titulo);
            String out = runAndCapture(cmd::ejecutar);
            var listar = new ListarRolesFaltantesCancionCommand(cancionService, titulo);
            String estado = runAndCapture(listar::ejecutar);
            refreshConsola("✅ Contratación - Canción: " + titulo, 
                          out + "\n─────────────────────────────────────\n" + estado, 
                          "Contratación procesada exitosamente");
            actualizarStatus("✅ Contratación completada");
        });
    }

    private void opcionContratarRecital() {
        actualizarStatus("💼 Procesando contratación masiva del recital...");
        var cmd = new ContratarArtistasParaRecitalCommand(artistaService, cancionService);
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("✅ Contratación Masiva - Recital Completo", out, "Proceso de contratación finalizado");
        actualizarStatus("✅ Contratación masiva completada");
    }

    private void opcionEntrenarArtista() {
        Dialog<Pair<String, String>> dlg = crearDialogoEntrenarConCombo();

        var result = dlg.showAndWait();
        if (result.isEmpty()) return;

        String nombre = result.get().getKey();
        String rol = result.get().getValue();

        actualizarStatus("💪 Entrenando a: " + nombre);
        
        var cmd = new EntrenarArtistaCommand(artistaService, nombre, rol);
        String out = runAndCapture(cmd::ejecutar);

        refreshConsola("💪 Entrenamiento - " + nombre, out, null);
        actualizarStatus("✅ Entrenamiento completado");
    }

    private void opcionListarContratados() {
        actualizarStatus("📋 Generando listado de artistas...");
        var cmd = new ListarArtistasCommand(artistaService);
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("👥 Artistas Contratados del Recital", out, "Listado generado correctamente");
        actualizarStatus("✅ Listado generado");
    }

    private void opcionListarCanciones() {
        actualizarStatus("📋 Generando listado de canciones ...");
        var cmd = new ListarCancionesCommand();
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("🎵 Repertorio de Canciones del Recital", out, "Listado generado correctamente");
        actualizarStatus("✅ Listado generado");
    }
    
    private void reportarYsalir() {
        actualizarStatus("📋 Generando reporte del recital...");
        
        // no se donde esta la instancia del recital y me estoy confundiendo xd
        var cmd = new ReportarSalirCommand(Paths.get("..", "Data", "recitalFinal.json"), RecitalRepository.getInstance(),recitalService);
        String out = runAndCapture(cmd::ejecutar);
        refreshConsola("🎵 Reporte del Recital", out, "Generado correctamente");
        actualizarStatus("✅ Reporte generado");
        
        
     // 2. Crear una transición de pausa de 5 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        
        // 3. Definir la acción a ejecutar después de la pausa
        delay.setOnFinished(event -> {
            // Cierra la aplicación JavaFX de forma segura
            Platform.exit();
        });
        
        // 4. Iniciar el temporizador
        delay.play();
        
    }

    private void opcionConsultaProlog() {
    	TextInputDialog dlg = crearDialogoEstilizado("📋 Consultando cantidad mínima de entrenamientos necesarios ...", 
                "Ingrese costo base");
		dlg.showAndWait().ifPresent(costo -> {
		actualizarStatus("💼 Procesando cantidad mínima de entrenamientos bajo costo de: $" + costo);
		var cmd = new MostrarEntrenamientosMinimosCommand(prologService, Double.parseDouble(costo));
		String out = runAndCapture(cmd::ejecutar);
		refreshConsola("🔍 Resultado de Consulta Prolog", 
		out + "\n─────────────────────────────────────\n", 
		"Consulta ejecutada");
        actualizarStatus("✅ Consulta Prolog ejecutada");
		});
    }

    private TextInputDialog crearDialogoEstilizado(String titulo, String mensaje) {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle(titulo);
        dlg.setHeaderText(mensaje);
        dlg.setContentText("Entrada:");
        return dlg;
    }
    
    private Dialog<Pair<String, String>> crearDialogoEntrenarConCombo() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Entrenar Artista");
        dialog.setHeaderText("Complete los datos");

        ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);

        // Campo 1: Nombre
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del artista");

        // Campo 2: Rol (ComboBox)
        ComboBox<String> comboRol = new ComboBox<>();
        comboRol.getItems().addAll(
            "BAJO",
            "BATERIA",
            "GUITARRA_ELECTRICA",
            "VOZ_PRINCIPAL",
            "PIANO"
        );
        comboRol.setPromptText("Seleccione un rol a entrenar");

        // Ubicar en la grilla
        grid.add(new Label("Artista:"), 0, 0);
        grid.add(txtNombre,         1, 0);

        grid.add(new Label("Rol:"), 0, 1);
        grid.add(comboRol,          1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado
        dialog.setResultConverter(btn -> {
            if (btn == okButton) {
                return new Pair<>(txtNombre.getText(), comboRol.getValue());
            }
            return null;
        });

        return dialog;
    }
    
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
        sb.append("╔═══════════════════════════════════════════════════════════╗\n");
        sb.append("║  ").append(titulo).append("\n");
        sb.append("╠═══════════════════════════════════════════════════════════╣\n\n");
        if (body != null && !body.isBlank()) {
            sb.append(body.trim()).append("\n");
        } else {
            sb.append("  (sin resultados)\n");
        }
        sb.append("\n╠═══════════════════════════════════════════════════════════╣\n");
        sb.append("║  ⏰ ").append(LocalDateTime.now().format(TS)).append("\n");
        if (extraPie != null && !extraPie.isBlank()) {
            sb.append("║  💡 ").append(extraPie).append("\n");
        }
        sb.append("╚═══════════════════════════════════════════════════════════╝\n");

        String texto = sb.toString();
        historial.agregar(texto);

        consola.clear();
        consola.setText(texto);
        consola.positionCaret(consola.getText().length());
    }
    
    // Clase auxiliar para items de menú
    private static class MenuItem {
        String icono;
        String texto;
        Runnable accion;
        
        MenuItem(String icono, String texto, Runnable accion) {
            this.icono = icono;
            this.texto = texto;
            this.accion = accion;
        }
    }
}