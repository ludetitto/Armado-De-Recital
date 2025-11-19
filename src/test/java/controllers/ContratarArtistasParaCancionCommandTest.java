package controllers;

import domain.*;
import repository.ArtistaRepository;
import repository.CancionRepository;
import repository.FuenteRecital;
import repository.JsonFuenteArtista;
import repository.JsonFuenteCancion;
import repository.JsonFuenteRecital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ContratarArtistasParaCancionCommandTest {

    
	private final Path rutaJsonReal = Paths.get("data", "recital.json"),
				  	   rutaJsonArtista = Paths.get("data", "artista.json"),
				  	   rutaJsonCancion = Paths.get("data", "canciones.json");
	
	private final String tituloEsperado = "LIVE AID";
	private ArtistaRepository artistaRepository = new ArtistaRepository();
    private CancionRepository cancionRepository = new CancionRepository();
    
    private static final String TITULO_CANCION = "Rocket Man";
    @BeforeEach
    void setUp() throws Exception {

        Recital.getInstance().setTitulo(tituloEsperado);

        new JsonFuenteArtista(rutaJsonArtista, artistaRepository);
        new JsonFuenteCancion(rutaJsonCancion, cancionRepository);
        
		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal, artistaRepository, cancionRepository);
		fuenteEntrada.cargar();
        
    }
    
    @Test
    void testEjecutarYDeshacerContratacionDebeSerCompletadaYRevertida() {

        Recital recital = Recital.getInstance();
        Cancion cancion = recital.obtenerCancionPorNombre(TITULO_CANCION);

        int rolesRequeridosTotal = cancion.getRolesRequeridos().size(); 
        int contratacionesAntes = recital.getContrataciones().size(); 

        assertEquals(rolesRequeridosTotal, cancion.getRolesFaltantes().size(), 
                     "Error en Pre-condición: Al inicio, los roles requeridos deben ser los faltantes.");


        ContratarArtistasParaCancionCommand command = new ContratarArtistasParaCancionCommand(TITULO_CANCION);
        command.ejecutar();

        int contratacionesDespues = recital.getContrataciones().size();
        
        assertEquals(contratacionesAntes + rolesRequeridosTotal, contratacionesDespues,
                     "El número total de contrataciones en el Recital no aumentó correctamente.");

        assertEquals(0, cancion.getRolesFaltantes().size(), 
                     "La canción debería tener 0 roles faltantes después de la ejecución exitosa.");


        command.deshacer();

        int contratacionesDespuesUndo = recital.getContrataciones().size();
        int rolesFaltantesDespuesUndo = cancion.getRolesFaltantes().size();

        assertEquals(contratacionesAntes, contratacionesDespuesUndo, 
                     "El número total de contrataciones en el Recital no volvió al estado inicial.");
        
        assertEquals(rolesRequeridosTotal, rolesFaltantesDespuesUndo, 
                     "La canción no revirtió su estado a roles faltantes correctamente.");
    }
}