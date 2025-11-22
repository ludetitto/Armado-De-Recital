package controllers;

import repository.RecitalLoaderTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;

import static org.junit.jupiter.api.Assertions.*;

class ListarRolesFaltantesCancionCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { 
    	RecitalLoaderTest.cargarDatos();
    }

    @Test
    void bohemianNoDebeTenerRolesFaltantes() {
        ListarRolesFaltantesCancionCommand cmd =
                new ListarRolesFaltantesCancionCommand("Bohemian Rhapsody");

        cmd.ejecutar();
        String o = out();

        assertTrue(o.contains("¡Roles cubiertos!") ||
                   o.contains("COMPLETA"),
                   "BR NO debe mostrar roles faltantes");
    }

    @Test
    void withOrWithoutYouDebeMostrarRolesFaltantesCorrectos() {
        ListarRolesFaltantesCancionCommand cmd =
                new ListarRolesFaltantesCancionCommand("With or Without You");

        cmd.ejecutar();
        String o = out();

        assertTrue(o.contains("GUITARRA_ELECTRICA"), "Falta GE según JSON");
        assertTrue(o.contains("BAJO"), "Falta BAJO según JSON");
    }
}
