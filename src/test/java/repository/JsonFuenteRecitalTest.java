package repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class JsonFuenteRecitalTest {

    private final String rutaJsonReal = "C:\\\\Users\\\\Milagros quispe\\\\Documents\\\\GitHub\\\\ParadigmasDeProgramacion\\\\practicaGrupal\\\\Armado-De-Recital\\\\Data\\\\recital.json";
    
    // Valores de verificación
    private final String tituloEsperado = "Queen Greatest Hits Setlist V3";
    private final int cantidadCancionesEsperada = 4;
    
    private final String rutaJsonSalida = "C:\\\\Users\\\\ Milagros quispe\\\\Documents\\\\GitHub\\\\ParadigmasDeProgramacion\\\\practicaGrupal\\\\Armado-De-Recital\\\\Data";
    private final String tituloPrueba = "Setlist Guardado y Verificado";
    private RecitalRepository recitalInstancia;
    
    // metodo para resetear el Singleton antes/después de cada test
    @BeforeEach
    @AfterEach
    void resetSingleton() throws Exception {

        java.lang.reflect.Field field = RecitalRepository.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
        
        recitalInstancia = RecitalRepository.getInstancia(tituloPrueba);
        recitalInstancia.setTitulo(tituloPrueba);
    }
    

    @AfterEach
    void cleanup() throws Exception {
        // Limpieza: Asegura el aislamiento entre pruebas
        java.lang.reflect.Field field = RecitalRepository.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
        
        Files.deleteIfExists(Paths.get(rutaJsonSalida));
    }
    
   
    @Test
    void testGuardarRecitalGeneraArchivoYSePuedeRecargar() throws Exception {
    	
        FuenteRecital fuenteGuardar = new JsonFuenteRecital(rutaJsonSalida);
        File archivoSalida = new File(rutaJsonSalida);
        Files.deleteIfExists(archivoSalida.toPath()); 
        
        List<RecitalRepository> listaRecitalesAGuardar = List.of(recitalInstancia);

        fuenteGuardar.guardar(listaRecitalesAGuardar);
        
        assertTrue(archivoSalida.exists(), "El método 'guardar' debe haber creado el archivo JSON.");
        assertTrue(archivoSalida.length() > 0, "El archivo JSON creado no debe estar vacío.");

        cleanup(); 

        FuenteRecital fuenteRecarga = new JsonFuenteRecital(rutaJsonSalida);

        List<RecitalRepository> recargados = fuenteRecarga.cargar();
        
        assertFalse(recargados.isEmpty(), "El archivo guardado debe poder ser recargado exitosamente.");
        assertEquals(1, recargados.size(), "Solo se debe recargar un recital de la lista.");
        assertEquals(tituloPrueba, recargados.get(0).getTitulo(), 
                     "El título del recital recargado debe ser el mismo que el que se guardó.");
    }


    @Test
    void testCargarRecitalExitoso() {
        FuenteRecital fuente = new JsonFuenteRecital(rutaJsonReal);
        
        fuente.cargar();

        RecitalRepository singleton = RecitalRepository.getInstancia("Titulo Ignorado");
        

        assertNotNull(singleton, "El Singleton debe haber sido inicializado después de una carga exitosa.");
        assertEquals(tituloEsperado, singleton.getTitulo(), "El título cargado debe coincidir con el JSON.");
        assertEquals(cantidadCancionesEsperada, singleton.getCanciones().size(), 
                     "La cantidad de canciones cargadas debe ser correcta.");
    }
    
 
//    @Test
//    void testCargarRecitalFallo() {
//        String rutaInvalida = "/ruta/imposible/archivo_que_no_existe.json";
//        FuenteRecital fuente = new JsonFuenteRecital(rutaInvalida);
//
//        assertThrows(RuntimeException.class, () -> fuente.cargar(), 
//                     "La implementación debe fallar al intentar leer un archivo inexistente.");
//
//        assertNull(RecitalRepository.getInstancia("Titulo Ignorado"), 
//                   "El Singleton NO debe ser inicializado tras un fallo de carga.");
//    }


}