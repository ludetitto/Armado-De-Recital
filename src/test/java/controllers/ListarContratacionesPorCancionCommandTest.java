package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;
import repository.RecitalLoaderTest;

import static org.junit.jupiter.api.Assertions.*;

class ListarContratacionesPorCancionCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarDatos();
    }

    @Test
    void listarContratacionesArtistaBase() {
        ListarContratacionesPorCancionCommand cmd =
                new ListarContratacionesPorCancionCommand();
        cmd.ejecutar();

        String out = out();

        assertTrue(out.contains("Bohemian Rhapsody"));
        assertTrue(out.contains("Freddie Mercury"));
        assertTrue(out.contains("VOZ_PRINCIPAL"));
        assertTrue(out.contains("0.0"));
    }

    @Test
    void listarContratacionesArtistaExternoConEntrenamiento() {
    	EntrenarArtistaCommand cmdEntrenar = new EntrenarArtistaCommand("Tina Turner", "COROS");
        
        cmdEntrenar.ejecutar();
        
        ContratarArtistasParaCancionCommand cmdContratar = new ContratarArtistasParaCancionCommand("Do They Know It's Christmas");
        
        cmdContratar.ejecutar();

        ListarContratacionesPorCancionCommand cmd = new ListarContratacionesPorCancionCommand();
        
        cmd.ejecutar();
        
        String out = out();

        assertTrue(out.contains("COROS asignado a Tina Turner con un costo individual de 4500.0"));
    }
    
    @Test
    void listarContratacionesArtistaExternoConColaboracion() {
        
        ContratarArtistasParaCancionCommand cmdContratar = new ContratarArtistasParaCancionCommand("Do They Know It's Christmas");
        
        cmdContratar.ejecutar();

        EntrenarArtistaCommand cmdEntrenar = new EntrenarArtistaCommand("Pete Townshend", "COROS");
        
        cmdEntrenar.ejecutar();
        
        cmdContratar.ejecutar();
        
        ListarContratacionesPorCancionCommand cmd = new ListarContratacionesPorCancionCommand();
        
        cmd.ejecutar();
        
        String out = out();

        assertTrue(out.contains("COROS asignado a Pete Townshend con un costo individual de 975.0"));
    }
}
