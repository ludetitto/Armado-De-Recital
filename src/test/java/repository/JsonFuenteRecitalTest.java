package repository;

import domain.Cancion;
import domain.Recital;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

class JsonFuenteRecitalTest {

	private final Path rutaJsonReal = Paths.get("..", "Data", "recital.json");
	private final String tituloEsperado = "Queen Greatest Hits Setlist V3";

	private final Path rutaJsonSalida = Paths.get("..", "Data", "recitalprueba.json");
	private final String tituloPrueba = "Queen Greatest Hits Setlist V3";

	@BeforeEach
	void setUp() throws Exception {
		java.lang.reflect.Field repoField = RecitalRepository.class.getDeclaredField("instance");
		repoField.setAccessible(true);
		repoField.set(null, null);

		java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
		recitalField.setAccessible(true);
		recitalField.set(null, null);

		RecitalRepository.getInstance().getRecital().setTitulo(tituloPrueba);
	}


	@AfterEach
	void tearDown() throws Exception {
		java.lang.reflect.Field repoField = RecitalRepository.class.getDeclaredField("instance");
		repoField.setAccessible(true);
		repoField.set(null, null);

		java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
		recitalField.setAccessible(true);
		recitalField.set(null, null);

		Files.deleteIfExists(rutaJsonSalida);
	}


	@Test
	void testGuardar() throws Exception {
		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal);
		fuenteEntrada.cargar();

		RecitalRepository repositorioActual = RecitalRepository.getInstance();

		assertFalse(repositorioActual.getRecital().getCanciones().isEmpty());
		assertEquals(tituloEsperado, repositorioActual.getRecital().getTitulo());

		FuenteRecital fuenteSalida = new JsonFuenteRecital(rutaJsonSalida);
		fuenteSalida.guardar(List.of(repositorioActual));

		File archivoSalida = rutaJsonSalida.toFile();
		assertTrue(archivoSalida.exists());
		assertTrue(archivoSalida.length() > 0);

		setUp();

		FuenteRecital fuenteVerificacion = new JsonFuenteRecital(rutaJsonSalida);
		fuenteVerificacion.cargar();

		RecitalRepository repositorioVerificado = RecitalRepository.getInstance();

		assertEquals(tituloEsperado, repositorioVerificado.getRecital().getTitulo());
	}
	
	@Test
    void testCargarYMostrarAtributosDeRecital() {
        // PREPARACIÓN
        FuenteRecital fuente = new JsonFuenteRecital(rutaJsonReal);
        fuente.cargar(); 

        // OBTENER INSTANCIA CARGADA
        Recital recitalCargado = Recital.getInstance();

        // --- ASERCIONES DE VERIFICACIÓN ---
        assertNotNull(recitalCargado, "La instancia de Recital no debe ser nula después de la carga.");
        assertEquals(tituloEsperado, recitalCargado.getTitulo(),
                     "El Recital Singleton no se actualizó con el título del JSON.");
        
        // --- MOSTRAR ATRIBUTOS POR CONSOLA (VERIFICACIÓN MANUAL) ---
        
        System.out.println("\n--- 🔎 INSPECCIÓN DE ATRIBUTOS DEL RECITAL CARGADO ---");
        System.out.println("Título: " + recitalCargado.getTitulo());
        System.out.println("Cantidad de Canciones: " + recitalCargado.getCanciones().size());
        System.out.println("Cantidad de Contrataciones: " + recitalCargado.getContrataciones().size());
        System.out.println("Cantidad de Artistas Base: " + recitalCargado.getArtistasBase().size());
        System.out.println("-----------------------------------------------------");

        // Detalle de Canciones y Roles Faltantes
        System.out.println("\n--- Detalle de Canciones y su Estado ---");
        for (Cancion cancion : recitalCargado.getCanciones()) {
            System.out.println(" > Canción: " + cancion.getTitulo());
            
            // Calculamos los faltantes dinámicamente
            Map<domain.TipoRol, Integer> faltantes = cancion.getRolesFaltantes();
            
            if (faltantes.isEmpty()) {
                System.out.println("Estado: COMPLETA (Roles cubiertos)");
            } else {
                System.out.println("Estado: INCOMPLETA (Roles faltantes: " + faltantes + ")");
            }
        }
        System.out.println("--------------------------------------------------\n");
    }


	@Test
	void testCargarExitoso() {
		FuenteRecital fuente = new JsonFuenteRecital(rutaJsonReal);

		List<RecitalRepository> resultadoCarga = fuente.cargar();

		RecitalRepository singleton = RecitalRepository.getInstance();

		assertFalse(resultadoCarga.isEmpty());
		assertEquals(tituloEsperado, singleton.getRecital().getTitulo());
	}

	@Test
	void testCargarFallo() {
		Path rutaInvalida = Paths.get("Data", "archivo_que_no_existe.json");
		FuenteRecital fuente = new JsonFuenteRecital(rutaInvalida);

		List<RecitalRepository> resultado = fuente.cargar();

		assertTrue(resultado.isEmpty());

		assertEquals(tituloPrueba, RecitalRepository.getInstance().getRecital().getTitulo());
	}
	
}