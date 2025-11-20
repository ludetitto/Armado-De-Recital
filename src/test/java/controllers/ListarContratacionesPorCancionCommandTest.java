package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;
import repository.RecitalLoaderTest;

import static org.junit.jupiter.api.Assertions.*;

class ListarContratacionesPorCancionCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarRecital(); 
    }

    @Test
    void listarContratacionesDebeMostrarContratacionesReales() {
        ListarContratacionesPorCancionCommand cmd =
                new ListarContratacionesPorCancionCommand();
        cmd.ejecutar();

        String out = out();

        assertTrue(out.contains("Bohemian Rhapsody"));
        assertTrue(out.contains("Freddie Mercury"));
        assertTrue(out.contains("VOZ_PRINCIPAL"));
        assertTrue(out.contains("0.0"));
    }

    
    // TODO: Mostrar y validar costos despues de contratación masiva
}
