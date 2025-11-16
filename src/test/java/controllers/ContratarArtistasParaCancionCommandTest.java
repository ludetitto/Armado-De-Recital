package controllers;

import domain.*;
import services.ArtistaService;
import services.CancionService;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ContratarArtistasParaCancionCommandTest {

    
	private final Path rutaJsonReal = Paths.get("..", "data", "recitalBandas_v3.json");
	private final String tituloEsperado = "LIVE AID";
    private ArtistaService artistaService;
    private CancionService cancionService;
    
    private static final String TITULO_CANCION = "With or Without You";
    private static final TipoRol ROL_FALTANTE = TipoRol.BAJO;

    @BeforeEach
    void setUp() throws Exception {

        RecitalRepository.getInstance().getRecital().setTitulo(tituloEsperado);
        
        this.artistaService = new ArtistaService();
        this.cancionService = new CancionService(); 

		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal);
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