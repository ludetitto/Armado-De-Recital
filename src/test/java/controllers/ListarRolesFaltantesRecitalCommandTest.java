package controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;
import repository.RecitalLoaderTest;

import static org.junit.jupiter.api.Assertions.*;

class ListarRolesFaltantesRecitalCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarDatos();
    }

    @Test
    void rolesFaltantesBohemianDebeDarCero() {
        ListarRolesFaltantesCancionCommand cmd =
                new ListarRolesFaltantesCancionCommand("Bohemian Rhapsody");

        cmd.ejecutar();
        String out = out();

        assertTrue(out.contains("¡Roles cubiertos! La canción está lista para tocarse."));

        assertFalse(out.contains("VOZ_PRINCIPAL"));
        assertFalse(out.contains("PIANO"));
        assertFalse(out.contains("GUITARRA_ELECTRICA"));
    }

}
