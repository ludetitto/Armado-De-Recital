package controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;
import repository.RecitalLoaderTest;

import static org.junit.jupiter.api.Assertions.*;

class ListarCancionesCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarDatos(); 
    }

    @Test
    void listaCancionesConEstadoCorrecto() {
        ListarCancionesCommand cmd = new ListarCancionesCommand();
        cmd.ejecutar();

        String o = out();

        assertTrue(o.contains("Bohemian Rhapsody"), "Debe listar BR");
        assertTrue(o.contains("COMPLETA"), "BR es completa según JSON");
        
        assertTrue(o.contains("With or Without You"), "Debe listar WOWY");
        assertTrue(o.contains("INCOMPLETA"), "WOWY está incompleta");
    }
}
