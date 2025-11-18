package controllers;

import domain.Recital;
import repository.ArtistaRepository;
import repository.CancionRepository;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ListarRolesFaltantesRecitalCommandTest { 
    
    // Reintroducimos la captura de salida para verificar el formato
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    private ArtistaRepository artistaRepository;
    private CancionRepository cancionRepository;
    
    private final Path rutaJsonReal = Paths.get("..", "Data", "recitalBandas_v3.json");
    private static final String TITULO_ESPERADO = "LIVE AID"; 
    
    
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
        
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @AfterEach
    void tearDown() throws Exception {
        System.setOut(standardOut);
        
        resetSingletons(); 
    }
    

    @Test
    void testEjecutar_ListaRolesFaltantesDetalladoDesdeJson() throws Exception {

    	FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal, artistaRepository, cancionRepository);
        fuenteEntrada.cargar(); 

        Recital recital = Recital.getInstance();
        assertEquals(TITULO_ESPERADO, recital.getTitulo()); 
        
     
        ListarRolesFaltantesRecitalCommand command = new ListarRolesFaltantesRecitalCommand();
        command.ejecutar();
        
        String output = outputStreamCaptor.toString().trim();
        
       
        standardOut.println("\n--- INICIO DE SALIDA DETALLADA CAPTURADA ---");
        standardOut.println(output);
        standardOut.println("--- FIN DE SALIDA DETALLADA CAPTURADA ---\n");
    
        
        // VERIFICACIÓN
        
    
        assertTrue(output.contains("--- Roles Faltantes por Canción (" + TITULO_ESPERADO + ") ---"));
        

        /* verificacion con el archivo inicial "recital"
        assertTrue(output.contains("[We Will Rock You]"));
        assertTrue(output.contains(" > BATERIA: Faltan 1"), "Debe mostrar que falta 1 BATERIA en WWRY.");
        
        assertTrue(output.contains("[Bohemian Rhapsody]"));
        assertTrue(output.contains(" > ¡Roles cubiertos!"), "Debe mostrar que los roles de Bohemian Rhapsody están cubiertos.");
        
        assertTrue(output.contains("Bohemian Rhapsody") && output.contains("We Will Rock You"), "Debe listar ambas canciones.");
        
        assertTrue(true, "La prueba de ejecución con JSON se completó y verificó el formato detallado.");
        */
    }
    

    

}