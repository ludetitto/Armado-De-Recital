package repository;

import domain.Artista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFuenteArtistaTest {

    private static final Path PATH_ENTRADA = Paths.get("..", "data", "artistas.json");
    private static final Path PATH_SALIDA = Paths.get("..", "data", "artistastest.json");
    private static final int CANTIDAD_ESPERADA = 8; 

    
    @BeforeEach
    void setUp() {
        File fileSalida = PATH_SALIDA.toFile();
        if (fileSalida.exists()) {
            fileSalida.delete();
        }
    }


    @Test
    void testCargarYVerificarRoles() {
        ArtistaRepository repository = new ArtistaRepository(); 
        
        JsonFuenteArtista fuente = new JsonFuenteArtista(PATH_ENTRADA, repository);
        fuente.cargar();
        
        assertEquals(CANTIDAD_ESPERADA, repository.cantidad());
        
        Artista georgeMichael = repository.buscarPorNombre("George Michael");
        assertNotNull(georgeMichael);
    }

    @Test
    void testGuardarYCargarCicloCompleto() {
        ArtistaRepository repoEntrada = new ArtistaRepository();
        JsonFuenteArtista fuenteEntrada = new JsonFuenteArtista(PATH_ENTRADA, repoEntrada);
        List<Artista> artistasIniciales = fuenteEntrada.cargar(); 
        
        JsonFuenteArtista fuenteSalida = new JsonFuenteArtista(PATH_SALIDA, repoEntrada);
        fuenteSalida.guardar(artistasIniciales); 
        
        ArtistaRepository repoVerificacion = new ArtistaRepository();
        
        JsonFuenteArtista fuenteVerificacion = new JsonFuenteArtista(PATH_SALIDA, repoVerificacion);
        List<Artista> artistasVerificados = fuenteVerificacion.cargar();

        assertEquals(artistasIniciales.size(), repoVerificacion.cantidad());
        assertEquals(CANTIDAD_ESPERADA, artistasVerificados.size());
    }
}