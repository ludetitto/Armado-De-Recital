package prolog;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.URI;
import java.nio.file.Path;
import java.util.Set;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import repository.ArtistaRepository;
import domain.Artista;
import domain.Cancion;
import domain.Recital;
import repository.CancionRepository;
import repository.JsonFuenteRecital;
import services.PrologService;

class PrologServiceTest {

	@BeforeAll
    static void requiereSwipl() throws Exception {
        Process p = new ProcessBuilder("swipl", "-q", "-g", "halt", "-t", "halt").start();
        boolean ok = p.waitFor(3, TimeUnit.SECONDS) && p.exitValue() == 0;
        assumeTrue(ok, "SWI-Prolog (swipl) no está disponible en PATH");
    }

    @Test
    void entrenamientosMinimos_conRepoReal_debeSer5() throws Exception {
        // 1) Cargar el Recital desde JSON al repositorio único
        URI uri = getClass().getResource("/data/recitalTest.json").toURI();
        ArtistaRepository artistaRepository = new ArtistaRepository();
        CancionRepository cancionRepository = new CancionRepository();
        
        new JsonFuenteRecital(Path.of(uri), artistaRepository, cancionRepository).cargar();

        // 2) Adaptadores simples a ArtistaRepository y CancionRepository usando el Recital cargado
        artistaRepository = new ArtistaRepository() {
            @Override public List<Artista> todos() {
                return Recital.getInstance().getArtistas();
            }
        };
        
        cancionRepository = new CancionRepository() {
			@Override public Set<Cancion> todas() {
                return Recital.getInstance().getCanciones();
            }
        };

        // 3) Ejecutar PrologService (lee reglas .pl de resources)
        PrologService prolog = new PrologService(artistaRepository, cancionRepository);
        int minimo = prolog.entrenamientosMinimos();

        // 4) Assert
        assertEquals(1, minimo);

    }
    
    @org.junit.jupiter.api.Test
    void recursoProlog_debeEstarEnClasspath() {
        var is = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("prolog/reglas_entrenamientos.pl");
        org.junit.jupiter.api.Assertions.assertNotNull(is,
            "Falta prolog/reglas_entrenamientos.pl en target/classes o target/test-classes");
    }

}
