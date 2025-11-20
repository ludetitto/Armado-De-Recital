package controllers;

import repository.RecitalLoaderTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.SimulacionConsola;

import static org.junit.jupiter.api.Assertions.*;

class EntrenarArtistaCommandTest extends SimulacionConsola {

    @BeforeEach
    void load() { RecitalLoaderTest.cargarRecital(); }

    @Test
    void entrenaRolNuevo() {
        EntrenarArtistaCommand cmd =
                new EntrenarArtistaCommand("Freddie Mercury", "GUITARRA_ELECTRICA");

        cmd.ejecutar();

        assertTrue(out().toLowerCase().contains("ahora posee"));
    }

    @Test
    void artistaNoExiste() {
        EntrenarArtistaCommand cmd =
                new EntrenarArtistaCommand("Maradona", "PIANO");

        cmd.ejecutar();

        assertTrue(out().toLowerCase().contains("no encontrado"));
    }
}
