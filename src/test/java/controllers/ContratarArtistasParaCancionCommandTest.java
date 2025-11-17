package controllers;

import domain.*;
import services.ArtistaService;
import services.CancionService;
import repository.ArtistaRepository;
import repository.CancionRepository;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ContratarArtistasParaCancionCommandTest {

    
	private final Path rutaJsonReal = Paths.get("../..", "data", "recitalBandas_v3.json");
	private final String tituloEsperado = "LIVE AID";
	private ArtistaRepository artistaRepository;
    private ArtistaService artistaService;
    private CancionRepository cancionRepository;
    private CancionService cancionService;
    
    private static final String TITULO_CANCION = "With or Without You";
    @BeforeEach
    void setUp() throws Exception {

        Recital.getInstance().setTitulo(tituloEsperado);

		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal, artistaRepository, cancionRepository);
		this.artistaService = new ArtistaService(artistaRepository);
        this.cancionService = new CancionService(); 
		fuenteEntrada.cargar();
        
    }
    
    @Test
    void testEjecutarYDeshacer_ContratacionDebeSerCompletadaYRevertida() {

        Recital recital = Recital.getInstance();

        int rolesFaltantesAntes = recital.getContrataciones().size();
        
        //System.out.println(rolesFaltantesAntes);

        ContratarArtistasParaCancionCommand command = new ContratarArtistasParaCancionCommand(artistaService, cancionService, TITULO_CANCION);

        command.ejecutar();


        int rolesFaltantesDespues = recital.getContrataciones().size();

        assertNotEquals(rolesFaltantesAntes, rolesFaltantesDespues);
        
        //ystem.out.println(rolesFaltantesDespues);

        command.deshacer();

        int rolesFaltantesDespuesUndo = recital.getContrataciones().size();
        
        assertNotEquals(rolesFaltantesDespuesUndo, rolesFaltantesDespues);

        //System.out.println(rolesFaltantesDespuesUndo);
    }
}