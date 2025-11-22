package repository;

import domain.Recital;
import domain.TipoDeArtista;
import domain.Artista;
import domain.Cancion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecitalRepositoryTest {

    private final Path PATH_ARTISTAS = Paths.get("Data", "artistas_v2.json");
    private final Path PATH_CANCIONES = Paths.get("Data", "canciones_v2.json");


    private static final int TOTAL_ARTISTAS_ESPERADOS = 19;
    private static final int TOTAL_CANCIONES_ESPERADAS = 15;
    
    private ArtistaRepository artistaRepository;
    private CancionRepository cancionRepository;

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

        this.artistaRepository = new ArtistaRepository();
        this.cancionRepository = new CancionRepository();
    }
    
    @AfterEach
    void tearDown() throws Exception {
        resetSingletons(); 
    }

    public void inicializarSistema() {

        JsonFuenteArtista fuenteArtista = new JsonFuenteArtista(PATH_ARTISTAS, artistaRepository);
        List<Artista> todosLosArtistas = fuenteArtista.cargar();


        JsonFuenteCancion fuenteCancion = new JsonFuenteCancion(PATH_CANCIONES, cancionRepository);
        List<Cancion> todasLasCanciones = fuenteCancion.cargar();


        Recital recital = Recital.getInstance();


        recital.agregarArtistas(todosLosArtistas); 
        recital.cargarCanciones(todasLasCanciones); 
    }

    // TODO: Revisar por qué falla
    
    @Test
    void testIntegracionCargaDeArchivosYRecitalSingleton() {

        inicializarSistema(); 


        Recital recitalCargado = Recital.getInstance();

        List<Artista> artistasBase = recitalCargado.getArtistasBase();
        List<Artista> artistasCandidatos = recitalCargado.getArtistasCandidatos();
        
        assertEquals(5, artistasBase.size(), "El Recital debe tener 5 artistas BASE.");
        assertEquals(14, artistasCandidatos.size(), "El Recital debe tener 15 artistas CANDIDATOS.");
        assertEquals(TOTAL_ARTISTAS_ESPERADOS, artistasBase.size() + artistasCandidatos.size(), "El total de artistas debe ser 20.");
        
        assertEquals(TOTAL_CANCIONES_ESPERADAS, recitalCargado.getCanciones().size(), 
                     "El Recital debe haber cargado 15 canciones del setlist.");
                     
        Artista eltonJohn = artistasCandidatos.stream()
            .filter(a -> a.getNombre().equals("Elton John"))
            .findFirst()
            .orElse(null);
            
        assertNotNull(eltonJohn, "Elton John debe estar en la lista de candidatos.");
        assertEquals(TipoDeArtista.EXTERNO, eltonJohn.getTipo());
    }
}