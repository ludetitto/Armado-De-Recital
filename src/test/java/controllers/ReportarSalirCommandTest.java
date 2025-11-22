package controllers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Recital;
import repository.ArtistaRepository;
import repository.CancionRepository;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;
import services.RecitalService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ReportarSalirCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private RecitalRepository recitalRepository = new RecitalRepository(Recital.getInstance());;
    private RecitalService realService;
    private ArtistaRepository repositoryArtistas = new ArtistaRepository();
	private CancionRepository repositoryCanciones= new CancionRepository();
	
    private final Path rutaJsonReal = Paths.get("Data", "recital.json");

	private final Path rutaJsonSalida = Paths.get("Data", "recitalTest.json");

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outContent));
        realService = new RecitalService();
        
        try {
        	
			FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal,repositoryArtistas, repositoryCanciones);
			recitalRepository=fuenteEntrada.cargar();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear archivo temporal", e);
        }
    }

    @AfterEach
	void tearDown() throws Exception {

		java.lang.reflect.Field recitalField = Recital.class.getDeclaredField("instance");
		recitalField.setAccessible(true);
		recitalField.set(null, null);

		Files.deleteIfExists(rutaJsonSalida);
	}

    @Test
    void ejecutar_conRutaValida_debeCrearArchivoReporte() {
        ReportarSalirCommand command = new ReportarSalirCommand(rutaJsonSalida, recitalRepository, realService);
        command.ejecutar();
        File outputFile = rutaJsonSalida.toFile();
        assertTrue(outputFile.exists());
    }

    @Test
    void ejecutar_conRutaNula_debeImprimirErrorYNoCrearArchivo() {
        ReportarSalirCommand command = new ReportarSalirCommand(null, recitalRepository, realService);
        command.ejecutar();
        String output = outContent.toString();
        assertTrue(output.contains("Error: La ruta para guardar los datos de recital no es válida."));
    }


}