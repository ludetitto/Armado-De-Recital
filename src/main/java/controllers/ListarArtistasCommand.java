package controllers;

import domain.Artista;
import domain.Recital;

import java.util.List;

public class ListarArtistasCommand implements ComandoContratacion {
	
    @Override
    public void ejecutar() {
        List<Artista> artistas = Recital.getInstance().getArtistasBase();
        
        System.out.println("--- Artistas del Recital (" + artistas.size() + ") ---");
        
        if (artistas.isEmpty()) {
            System.out.println("No hay artistas cargados/visibles en el recital.");
        } else {
            for (Artista a : artistas) {
                System.out.println(" - " + a);
            }
        }
        
        List<Artista> artistasCandidatos = Recital.getInstance().getArtistasCandidatos();
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
