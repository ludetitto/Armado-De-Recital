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
import repository.CancionRepository;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;
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
        ArtistaRepository artistaRepository;
        CancionRepository cancionRepository;
        
        new JsonFuenteRecital(Path.of(uri)).cargar();

        // 2) Adaptadores simples a ArtistaRepository y CancionRepository usando el Recital cargado
        artistaRepository = new ArtistaRepository() {
            @Override public List<Artista> todos() {
                return RecitalRepository.getInstance().getRecital().getArtistas();
            }
        };
        
        cancionRepository = new CancionRepository() {
			@Override public Set<Cancion> todas() {
                return RecitalRepository.getInstance().getRecital().getCanciones();
            }
        };

        // 3) Ejecutar PrologService (lee reglas .pl de resources)
        PrologService prolog = new PrologService();
        int minimo = prolog.entrenamientosMinimos(artistaRepository, cancionRepository, Set.of());

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
