package controllers;

import domain.Artista;
import domain.Recital;

import java.util.List;

public class ListarArtistasCommand implements ComandoContratacion {
	
    @Override
    public void ejecutar() {
        List<Artista> artistas = Recital.getInstance().getArtistasConContratacion();
        
        System.out.println("--- Artistas contratados para el Recital (" + artistas.size() + ") ---");
        
        if (artistas.isEmpty()) {
            System.out.println("No hay artistas contratados para el recital.");
        } else {
            for (Artista a : artistas) {
                System.out.println(" - " + a);
            }
        }
        
        List<Artista> todosArtistas = Recital.getInstance().getArtistas();
        System.out.println("\n--- Todos los Artistas Disponibles (" + todosArtistas.size() + ") ---");
        if (artistas.isEmpty()) {
            System.out.println("No hay artistas cargados/visibles en el recital.");
        } else {
            for (Artista a : todosArtistas) {
                System.out.println(" - " + a);
            }
        }
        System.out.println("---------------------------------------------");
    }

    @Override
    public void deshacer() {
    }
}
