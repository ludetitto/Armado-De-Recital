package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;
import repository.RecitalLoaderTest;

import static org.junit.jupiter.api.Assertions.*;

class ListarArtistasCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarDatos();
    }

    @Test
    void listaArtistasDeBaseYExternos() {
        ListarArtistasCommand cmd = new ListarArtistasCommand();
        cmd.ejecutar();

        String o = out();

        assertTrue(o.contains("Freddie Mercury"), "BASE");
        assertTrue(o.contains("Brian May"));
        assertTrue(o.contains("Bono"));

        assertTrue(o.contains("Madonna"), "Viene en artistas_v2.json");
    }
}