package controllers;

import domain.Artista;
import services.ArtistaService;

import java.util.List;

public class ListarArtistasCommand implements ComandoContratacion {
	ArtistaService artistaService;
	
    // Firma que usa tu Menu (sin args)
    public ListarArtistasCommand() {}

    // Sobrecarga por si después querés inyectar servicio
    public ListarArtistasCommand(services.ArtistaService artistaService) {
    	this.artistaService = artistaService;
    }

    @Override
    public void ejecutar() {
        List<Artista> artistas = artistaService.getArtistasBase();
        System.out.println("--- Artistas del Recital (" + artistas.size() + ") ---");
        if (artistas.isEmpty()) {
            System.out.println("No hay artistas cargados/visibles en el recital.");
        } else {
            for (Artista a : artistas) {
                System.out.println(" - " + a);
            }
        }
        
        List<Artista> artistasCandidatos = artistaService.getArtistasCandidatos();
        System.out.println("--- Artistas candidatos (" + artistasCandidatos.size() + ") ---");
        if (artistas.isEmpty()) {
            System.out.println("No hay artistas cargados/visibles en el recital.");
        } else {
            for (Artista a : artistasCandidatos) {
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
