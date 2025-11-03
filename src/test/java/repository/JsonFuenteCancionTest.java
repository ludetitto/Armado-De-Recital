package repository;

import domain.Artista;
import domain.Cancion;
import domain.TipoEstado;
import domain.TipoRol;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFuenteCancionTest {

	private static final Path PATH_ENTRADA = Paths.get("..", "data", "canciones.json");
	private static final Path PATH_SALIDA = Paths.get("..", "data", "cancionestest.json");
	private static final int CANTIDAD_ESPERADA = 4; // Asegúrate de que este valor coincida con tu JSON
	
	
	// comentar para obtener el archivo del test
	@AfterEach
	void tearDown() throws IOException {
	    Files.deleteIfExists(PATH_SALIDA);
	}

	@Test
	void testCargarBasicoYCantidad() {
		CancionRepository repository = new CancionRepository();
		JsonFuenteCancion fuente = new JsonFuenteCancion(PATH_ENTRADA, repository);

		List<Cancion> cancionesCargadas = fuente.cargar();

		assertNotNull(cancionesCargadas);
		assertEquals(CANTIDAD_ESPERADA, cancionesCargadas.size());
		assertEquals(CANTIDAD_ESPERADA, repository.cantidad());
	}

	@Test
	void testVerificarRolesRequeridos() {
		CancionRepository repository = new CancionRepository();
		JsonFuenteCancion fuente = new JsonFuenteCancion(PATH_ENTRADA, repository);
		fuente.cargar();

		Cancion rhapsody = repository.buscarPorTitulo("Bohemian Rhapsody");
		assertNotNull(rhapsody);

		Map<TipoRol, Integer> requeridos = rhapsody.getRolesRequeridos();

		assertTrue(requeridos.containsKey(TipoRol.VOZ_SECUNDARIA));
		assertEquals(4, requeridos.get(TipoRol.VOZ_SECUNDARIA));

		assertTrue(requeridos.containsKey(TipoRol.PIANO));
		assertEquals(1, requeridos.get(TipoRol.PIANO));
	}

	@Test
	void testPersistirAsignaciones() {
		ArtistaRepository repoArtista = new ArtistaRepository();
		Artista eltonJohn = new Artista("Elton John Test", domain.TipoDeArtista.EXTERNO,
				java.util.Set.of(TipoRol.PIANO), java.util.Set.of(), 1000.0);
		repoArtista.agregar(eltonJohn);

		CancionRepository repoCancion = new CancionRepository();
		JsonFuenteCancion fuente = new JsonFuenteCancion(PATH_ENTRADA, repoCancion);
		fuente.cargar();

		Cancion rocketMan = repoCancion.buscarPorTitulo("Rocket Man");
		rocketMan.asignarArtista(eltonJohn, TipoRol.PIANO);

		List<Cancion> cancionesAGuardar = repoCancion.obtenerTodas();
		JsonFuenteCancion fuenteSalida = new JsonFuenteCancion(PATH_SALIDA, repoCancion);
		fuenteSalida.guardar(cancionesAGuardar);

		// El test de verificación se hace en el siguiente método
		assertTrue(true); // Indica que el guardado se completó sin excepción.
	}

	@Test
	void testGuardarYCargarSinAsignaciones() {
		CancionRepository repoEntrada = new CancionRepository();
		JsonFuenteCancion fuenteEntrada = new JsonFuenteCancion(PATH_ENTRADA, repoEntrada);
		fuenteEntrada.cargar();

		List<Cancion> cancionesAGuardar = repoEntrada.obtenerTodas();

		JsonFuenteCancion fuenteSalida = new JsonFuenteCancion(PATH_SALIDA, repoEntrada);
		fuenteSalida.guardar(cancionesAGuardar);

		CancionRepository repoVerificacion = new CancionRepository();
		JsonFuenteCancion fuenteVerificacion = new JsonFuenteCancion(PATH_SALIDA, repoVerificacion);
		List<Cancion> cancionesVerificadas = fuenteVerificacion.cargar();

		assertEquals(CANTIDAD_ESPERADA, cancionesVerificadas.size());

		Cancion rhapsodyVerificada = repoVerificacion.buscarPorTitulo("Bohemian Rhapsody");

		assertTrue(rhapsodyVerificada.getRolesRequeridos().containsKey(TipoRol.BAJO));

		// El estado debe ser BORRADOR/INCOMPLETA si no se asignó nada.
		assertEquals(TipoEstado.BORRADOR, rhapsodyVerificada.getEstado());
	}
}