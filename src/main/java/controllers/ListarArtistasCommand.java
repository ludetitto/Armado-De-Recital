package controllers;

import domain.Artista;
import domain.Recital;

import java.util.List;

public class ListarArtistasCommand implements ComandoContratacion {

    // Firma que usa tu Menu (sin args)
    public ListarArtistasCommand() {}

    // Sobrecarga por si después querés inyectar servicio
    public ListarArtistasCommand(services.ArtistaService artistaService) {}

    @Override
    public void ejecutar() {
        List<Artista> artistas = Recital.getInstance().getArtistas();
        System.out.println("--- Artistas del Recital (" + artistas.size() + ") ---");
        if (artistas.isEmpty()) {
            System.out.println("No hay artistas cargados/visibles en el recital.");
        } else {
            for (Artista a : artistas) {
                System.out.println(" - " + a);
            }
        }
        System.out.println("---------------------------------------------");
    }

    @Override
    public void deshacer() {
        // no-op
    }
}
