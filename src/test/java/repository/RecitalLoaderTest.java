package repository;

import domain.Recital;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class RecitalLoaderTest {
	private static final Logger logger = Logger.getLogger(RecitalLoaderTest.class.getName());
	public static void cargarDatos() {

        ArtistaRepository artistaRepository = new ArtistaRepository();
        CancionRepository cancionRepository = new CancionRepository();

        try {
            // 1) RECITAL
            Path recitalPath = elegirArchivo("Data/recital_v2.json", "Data/recital.json");
            logger.info("Cargando recital desde: " + recitalPath.toAbsolutePath());
            FuenteRecital fuenteRecital = new JsonFuenteRecital(recitalPath, artistaRepository, cancionRepository);
            RecitalRepository recitalRepository = fuenteRecital.cargar();

            // 2) ARTISTAS
            Path artistasPath = elegirArchivo("Data/artistas_v2.json", "Data/artistas.json");
            logger.info("Cargando artistas desde: " + artistasPath.toAbsolutePath());
            FuenteArtista fuenteArtista = new JsonFuenteArtista(artistasPath, artistaRepository);
            fuenteArtista.cargar();

            // 3) CANCIONES
            Path cancionesPath = elegirArchivo("Data/canciones_v2.json", "Data/canciones.json");
            logger.info("Cargando canciones desde: " + cancionesPath.toAbsolutePath());
            FuenteCancion fuenteCancion = new JsonFuenteCancion(cancionesPath, cancionRepository);
            fuenteCancion.cargar();

            recitalRepository.setRecital(Recital.getInstance());

            logger.info("Datos de prueba cargados correctamente");

        } catch (Exception e) {
            logger.severe("Error al cargar datos de prueba: " + e.getMessage());
            throw new RuntimeException("Error al cargar datos de prueba", e);
        }
    }

    private static Path elegirArchivo(String preferido, String fallback) {
        Path pPreferido = Paths.get(preferido);
        if (Files.exists(pPreferido)) {
            return pPreferido;
        }
        return Paths.get(fallback);
    }
}