package controllers;

import domain.Recital;
import services.CancionService;
import repository.ArtistaRepository;
import repository.CancionRepository;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ListarRolesFaltantesCancionCommandTest { 
    
    // Configuración para la captura de la salida de consola
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private CancionService cancionService; 
    private CancionRepository cancionRepository;
    private ArtistaRepository artistaRepository;
    
    // Rutas y datos
    private final Path rutaJsonReal = Paths.get("..", "Data", "recital.json");
    private static final String CANCION_INCOMPLETA = "We Will Rock You";
    private static final String CANCION_COMPLETA = "Bohemian Rhapsody";
    private static final String CANCION_INEXISTENTE = "Cancion Inexistente";
    
    /** Reinicia las instancias Singleton de Recital y RecitalRepository. */
    private void resetSingletons() throws Exception {
        java.lang.reflect.Field repoField = RecitalRepository.class.getDeclaredField("instance");
        repoField.setAccessible(true);
        repoField.set(null, null);

        java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
        recitalField.setAccessible(true);
        recitalField.set(null, null);
    }

    @BeforeEach
    void setUp() throws Exception {
        resetSingletons();
        
        // Redirigir la salida estándar para capturar el texto
        System.setOut(new PrintStream(outputStreamCaptor));
        // ⬅️ CAMBIO 2: Inicializamos CancionService
        this.cancionService = new CancionService(null); 
        
        // Cargar el archivo JSON una sola vez antes de los tests
        FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal, artistaRepository, cancionRepository);
        fuenteEntrada.cargar(); 
    }
    
    @AfterEach
    void tearDown() throws Exception {
        // Restaurar la salida estándar original
        System.setOut(standardOut);
        resetSingletons(); 
    }
    
    
    @Test
    void testEjecutar_MuestraRolesFaltantesDeCancionIncompleta() throws Exception {
        ListarRolesFaltantesCancionCommand command = 
            new ListarRolesFaltantesCancionCommand(cancionService, CANCION_INCOMPLETA);
        command.ejecutar();
        
        // CAPTURA Y VISUALIZACIÓN
        String output = outputStreamCaptor.toString().trim();
        standardOut.println("\n--- SALIDA CAPTURADA DEL TEST DE CANCIÓN INCOMPLETA ---");
        standardOut.println(output);
        standardOut.println("----------------------------------------------------------\n");
        
        // VERIFICACIÓN
        assertTrue(output.contains("--- Roles Faltantes para: " + CANCION_INCOMPLETA + " ---"),
                   "Debe mostrar el encabezado de la canción.");
        
        assertTrue(output.contains(" > BATERIA: Faltan 1"), 
                   "Debe indicar que falta 1 BATERIA.");
        
        assertFalse(output.contains("¡Roles cubiertos!"), 
                    "No debe mostrar el mensaje de éxito.");
    }

    @Test
    void testEjecutar_MuestraRolesFaltantesDeCancionCompleta() throws Exception {

    	ListarRolesFaltantesCancionCommand command = 
            new ListarRolesFaltantesCancionCommand(cancionService, CANCION_COMPLETA);
        command.ejecutar();
        
        String output = outputStreamCaptor.toString().trim();
        
        // VISUALIZACIÓN
        standardOut.println("\n--- SALIDA CAPTURADA DEL TEST DE CANCIÓN COMPLETA ---");
        standardOut.println(output);
        standardOut.println("------------------------------------------------------\n");
        
        
    }
    
    @Test
    void testEjecutar_CancionInexistente() throws Exception {
        ListarRolesFaltantesCancionCommand command = 
            new ListarRolesFaltantesCancionCommand(cancionService, CANCION_INEXISTENTE);
        command.ejecutar();
        
        String output = outputStreamCaptor.toString().trim();
        
        standardOut.println("\n--- SALIDA CAPTURADA DEL TEST DE CANCIÓN INEXISTENTE ---");
        standardOut.println(output);
        standardOut.println("------------------------------------------------------\n");
        
        
    }

}