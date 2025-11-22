package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;
import repository.RecitalLoaderTest;

class MostrarEntrenamientosMinimosCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarDatos();
    }

    @Test
    void muestraEntrenamientosDesdeProlog() throws Exception {
        // TODO
    }
}

