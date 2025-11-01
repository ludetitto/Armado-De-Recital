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
    private final String tituloEsperado = "Queen Greatest Hits Setlist V3";
    private final int cantidadCancionesEsperada = 4;
    
    
    private final String rutaJsonSalida = "C:\\\\Users\\\\Milagros quispe\\\\Documents\\\\GitHub\\\\ParadigmasDeProgramacion\\\\practicaGrupal\\\\Armado-De-Recital\\\\Data\\\\recitalprueba.json";
    private final String tituloPrueba = "Setlist Guardado y Verificado";
    private RecitalRepository recitalInstancia;
    
    @BeforeEach
    void setUp() throws Exception {
        java.lang.reflect.Field field = RecitalRepository.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
        
        recitalInstancia = RecitalRepository.getInstancia(tituloPrueba);
        recitalInstancia.setTitulo(tituloPrueba);
    }
    

	/*
	 * @AfterEach void tearDown() throws Exception { java.lang.reflect.Field field =
	 * RecitalRepository.class.getDeclaredField("instance");
	 * field.setAccessible(true); field.set(null, null);
	 * 
	 * Files.deleteIfExists(Paths.get(rutaJsonSalida)); }
	 */
    
    
    @Test
    void testGuardarRecitalGeneraArchivoYSePuedeRecargar() throws Exception {
    	
    	//cargo lo que tengo rn recital.json
        FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal);
        List<RecitalRepository> resultadoCarga = fuenteEntrada.cargar();
        
        RecitalRepository singletonCargado = RecitalRepository.getInstancia(null);

        
        //guardo en el recitalprueba.json
        FuenteRecital fuenteSalida = new JsonFuenteRecital(rutaJsonSalida);
        File archivoSalida = new File(rutaJsonSalida);
        
        Files.deleteIfExists(archivoSalida.toPath());
        
        List<RecitalRepository> listaAGuardar = List.of(singletonCargado);

        fuenteSalida.guardar(listaAGuardar); 
        
        assertTrue(archivoSalida.exists());
        assertTrue(archivoSalida.length() > 0);
        
        setUp(); 
        
        FuenteRecital fuenteVerificacion = new JsonFuenteRecital(rutaJsonSalida);
        List<RecitalRepository> recargados = fuenteVerificacion.cargar();
        
        RecitalRepository repositorioVerificado = recargados.get(0);

        assertEquals(tituloEsperado, repositorioVerificado.getTitulo());
    }


    @Test
    void testCargarRecitalExitoso() {
        FuenteRecital fuente = new JsonFuenteRecital(rutaJsonReal);
        
        List<RecitalRepository> resultadoCarga = fuente.cargar();

        RecitalRepository singleton = RecitalRepository.getInstancia(null);
        

        assertFalse(resultadoCarga.isEmpty());
        assertNotNull(singleton);
        assertEquals(tituloEsperado, singleton.getTitulo());
        // Se asume que tienes un método getCanciones()
        // assertEquals(cantidadCancionesEsperada, singleton.getCanciones().size()); 
    }
    
 
    @Test
    void testCargarRecitalFallo() {
        String rutaInvalida = "/ruta/imposible/archivo_que_no_existe.json";
        FuenteRecital fuente = new JsonFuenteRecital(rutaInvalida);

        List<RecitalRepository> resultado = fuente.cargar();
        
        assertTrue(resultado.isEmpty());

        assertNull(RecitalRepository.getInstancia(null));
    }
}