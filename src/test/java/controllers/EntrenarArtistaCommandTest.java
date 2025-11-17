package controllers;

import domain.Artista;
import domain.Recital;
import domain.TipoRol;
import domain.TipoDeArtista;
import services.ArtistaService;
import services.RecitalService;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EntrenarArtistaCommandTest { 
	

	private final Path rutaJsonReal = Paths.get("..", "Data", "recitalBandas_v3.json");
	private final String tituloEsperado = "LIVE AID";

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    private ArtistaService artistaService;


    private static final String NOMBRE_CANDIDATO = "Elton John";
    private static final String NOMBRE_INEXISTENTE = "Lady Gaga";
    private static final TipoRol ROL_NUEVO = TipoRol.BATERIA;
    private static final TipoRol ROL_EXISTENTE = TipoRol.TECLADOS;


   
    @BeforeEach
    void setUp() throws Exception {
    	RecitalRepository.getInstance().getRecital().setTitulo(tituloEsperado);
        
        this.artistaService = new ArtistaService();

		FuenteRecital fuenteEntrada = new JsonFuenteRecital(rutaJsonReal, null, null);
		fuenteEntrada.cargar();
    }

    /* tengo que ver por que no me dela indicarle nulo a la instancia, pero como no hay ninguna instancia ahora no pasa nada?
    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        Recital.setInstance(null); 
    }
        */
    @Test
    void testEjecutar_EntrenamientoExitosoDeRolNuevo() {

    	
        EntrenarArtistaCommand command = new EntrenarArtistaCommand(artistaService, NOMBRE_CANDIDATO, ROL_NUEVO.name());

        
        command.ejecutar();

        
        Artista artista = Recital.getInstance().getArtistasCandidatos().stream()
                            .filter(a -> a.getNombre().equals(NOMBRE_CANDIDATO))
                            .findFirst().orElseThrow();

        
        assertTrue(artista.puedeOcuparRol(ROL_NUEVO), "El artista debe poder ocupar el nuevo rol despu�s del entrenamiento.");

 

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Entrenamiento aplicado: " + NOMBRE_CANDIDATO + " ahora puede " + ROL_NUEVO));
    }


    @Test
    void testEjecutar_LanzaExcepcionSiArtistaNoExiste() {

        EntrenarArtistaCommand command = new EntrenarArtistaCommand(artistaService, NOMBRE_INEXISTENTE, ROL_NUEVO.name());


        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                command.ejecutar();
            }
        }, "Debe lanzar IllegalArgumentException si el artista no se encuentra.");
    }
    
    @Test
    void testDeshacer_MuestraAdvertenciaDeNoReversion() {

        EntrenarArtistaCommand command = new EntrenarArtistaCommand(artistaService, NOMBRE_CANDIDATO, ROL_NUEVO.name());

        command.deshacer();

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("no es posible revertir el entrenamiento"), 
                   "Debe mostrar la advertencia de que el deshacer no est� implementado.");
    }
}