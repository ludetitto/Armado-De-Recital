package controllers;

import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import domain.Artista;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ListarCancionesCommandTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    private final String TITULO_RECITAL = "Recital de Prueba";
    
    // reiniciar el Singleton
    private void resetRecitalSingleton() throws Exception {

    	// RecitalRepository no es Singleton o se limpia por separado
        java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
        recitalField.setAccessible(true);
        recitalField.set(null, null);
    }

    @BeforeEach
    void setUp() throws Exception {
        // Redirigir la salida estándar
        System.setOut(new PrintStream(outputStreamCaptor));
        
        // Limpiar el Singleton antes de cada test
        resetRecitalSingleton();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        // Restaurar la salida estándar original
        System.setOut(standardOut);
        
        // Limpiar el Singleton después del test
        resetRecitalSingleton(); 
    }
    


    private void configurarRecitalConDatosMixtos() {
        Recital recital = Recital.getInstance();
        recital.setTitulo(TITULO_RECITAL);

        //base
        Artista brianMay = new Artista("Brian May", domain.TipoDeArtista.BASE, 
                                        Set.of(TipoRol.GUITARRA_ELECTRICA), Collections.emptySet(), 0.0);
        
        //1
        Cancion cancionCompleta = new Cancion("Bohemian Test", Map.of(TipoRol.GUITARRA_ELECTRICA, 1));
       
        cancionCompleta.asignarArtista(brianMay, TipoRol.GUITARRA_ELECTRICA);
        
        //2
        Cancion cancionIncompleta = new Cancion("Rock You Test", Map.of(TipoRol.BATERIA, 1, TipoRol.BAJO, 1));

        Artista johnDeacon = new Artista("John Deacon", domain.TipoDeArtista.BASE, 
                                          Set.of(TipoRol.BAJO), Collections.emptySet(), 0.0);
        cancionIncompleta.asignarArtista(johnDeacon, TipoRol.BAJO);
        
        // Agregar canciones al recital
        recital.agregarCancion(cancionCompleta);
        recital.agregarCancion(cancionIncompleta);
    }

    @Test
    void testListar_RecitalVacio() {


        Recital.getInstance().setTitulo(TITULO_RECITAL);
        
     
        ListarCancionesCommand command = new ListarCancionesCommand(); 
        command.ejecutar();

        String output = outputStreamCaptor.toString().trim();
        
        assertTrue(output.contains("--- Canciones del Recital (0) ---"));
        assertTrue(output.contains("No hay canciones cargadas en el recital."));
    }

    @Test
    void testListar_CancionesConEstadosMixtos() {
        
        configurarRecitalConDatosMixtos();
        
  
        ListarCancionesCommand command = new ListarCancionesCommand();
        command.ejecutar();
        

        String output = outputStreamCaptor.toString();
        

        assertTrue(output.contains("--- Canciones del Recital (2) ---"));
        
        assertTrue(output.contains("- Bohemian Test [COMPLETA]"));
        assertTrue(output.contains("Bohemian Test") && output.contains("¡Roles cubiertos!"), 
                   "La canción completa debe mostrar el mensaje de éxito.");

        assertTrue(output.contains("- Rock You Test [INCOMPLETA]"));
        assertTrue(output.contains("Rock You Test") && output.contains("BATERIA: faltan 1"), 
                   "La canción incompleta debe listar el rol faltante (BATERIA).");
       
        assertFalse(output.contains("BAJO: faltan"), 
                    "El rol BAJO no debe aparecer como faltante.");
    }
}