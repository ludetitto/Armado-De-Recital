package controllers;

import domain.Recital;
import services.ArtistaService;
import services.RecitalService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import repository.*;


import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;


public class ContratarArtistasParaRecitalCommandTest { 
    
	private final Path rutaJsonReal = Paths.get("..", "Data", "recitalBandas_v3.json");
	private final String tituloEsperado = "LIVE AID";

    private ArtistaService artistaService;
    private RecitalService recitalService;
    
    @BeforeEach
    void setUp() throws Exception {
        
        RecitalRepository.getInstance().getRecital().setTitulo(tituloEsperado);
        
        this.artistaService = new ArtistaService();
        this.recitalService = new RecitalService(); 

		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal);
		fuenteEntrada.cargar();
        
    }
    
    @AfterEach
    void tearDown() {
        Recital.setInstance(null); 
    }

    @Test
    void testEjecutarYDeshacer_ContratacionDebeSerRevertida() {

        int contratacionesIniciales = Recital.getInstance().getContrataciones().size(); 

        ContratarArtistasParaRecitalCommand command = 
            new ContratarArtistasParaRecitalCommand(this.artistaService, this.recitalService);
        
        command.ejecutar();
        
        int contratacionesDespuesEjecucion = Recital.getInstance().getContrataciones().size();

        assertTrue(contratacionesDespuesEjecucion > contratacionesIniciales, 
                   "La ejecución debe resultar en nuevas contrataciones para las canciones faltantes.");

        command.deshacer();
        
        int contratacionesDespuesUndo = Recital.getInstance().getContrataciones().size();

        assertEquals(contratacionesIniciales, contratacionesDespuesUndo, 
                     "El número de contrataciones debe volver al estado inicial después del deshacer.");
        
    }
}