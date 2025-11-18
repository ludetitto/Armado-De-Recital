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
import java.util.Map;

class JsonFuenteRecitalTest {

	private final Path rutaJsonReal = Paths.get("Data", "recital.json");
	private final String tituloEsperado = "Queen Greatest Hits Setlist V3";

	private final Path rutaJsonSalida = Paths.get("Data", "recitalTest.json");
	private final String tituloPrueba = "Queen Greatest Hits Setlist V3";
	
	private final ArtistaRepository repositoryArtistas = new ArtistaRepository(); 
    private final CancionRepository repositoryCanciones = new CancionRepository();
    private RecitalRepository repositoryRecital;

	@BeforeEach
	void setUp() throws Exception {

		java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
		recitalField.setAccessible(true);
		recitalField.set(null, null);

		repositoryRecital = new RecitalRepository(Recital.getInstance());
				
		Recital.getInstance().setTitulo(tituloPrueba);
	}

	// comentar para obtener el archivo test
	
	@AfterEach
	void tearDown() throws Exception {

		java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
		recitalField.setAccessible(true);
		recitalField.set(null, null);

		Files.deleteIfExists(rutaJsonSalida);
	}

	
	
	@Test
	void testGuardar() throws Exception {
		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal,repositoryArtistas, repositoryCanciones);
		repositoryRecital=fuenteEntrada.cargar();

		Recital recitalActual = Recital.getInstance();

		assertFalse(recitalActual.getCanciones().isEmpty());
		assertEquals(tituloEsperado, recitalActual.getTitulo());
		
		FuenteRecital fuenteSalida = new JsonFuenteRecital(rutaJsonSalida,repositoryArtistas, repositoryCanciones);
		fuenteSalida.guardar(repositoryRecital);


		File archivoSalida = rutaJsonSalida.toFile();
		assertTrue(archivoSalida.exists());
		assertTrue(archivoSalida.length() > 0);

		setUp();

		FuenteRecital fuenteVerificacion = new JsonFuenteRecital(rutaJsonSalida,repositoryArtistas, repositoryCanciones);
		fuenteVerificacion.cargar();

		Recital recitalVerificado = Recital.getInstance();

		assertEquals(tituloEsperado, recitalVerificado.getTitulo());
	}
	
	@Test
    void testCargarYMostrarAtributosDeRecital() {
        // PREPARACIÓN
        FuenteRecital fuente = new JsonFuenteRecital(rutaJsonReal,repositoryArtistas, repositoryCanciones);
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
        System.out.println("Cantidad de Artistas Externo: " + recitalCargado.getArtistasCandidatos().size());
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
		FuenteRecital fuente = new JsonFuenteRecital(rutaJsonReal,repositoryArtistas, repositoryCanciones);

		fuente.cargar();

		Recital singleton = Recital.getInstance();
		
		assertEquals(tituloEsperado, singleton.getTitulo());
	}

	@Test
	void testCargarFallo() {
		Path rutaInvalida = Paths.get("Data", "archivo_que_no_existe.json");
		FuenteRecital fuente = new JsonFuenteRecital(rutaInvalida,repositoryArtistas, repositoryCanciones);

		RecitalRepository resultado = fuente.cargar();

		assertTrue(resultado == null);

		assertEquals(tituloPrueba, Recital.getInstance().getTitulo());
	}
	
}


