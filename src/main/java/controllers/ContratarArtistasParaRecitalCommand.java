package controllers;

import java.util.Set;

import domain.Cancion;
import domain.Recital;
import services.ContratacionService;

public class ContratarArtistasParaRecitalCommand implements ComandoContratacion {

	private ContratacionService contratacionService;
	
    public ContratarArtistasParaRecitalCommand() {
    	contratacionService = new ContratacionService();
    }

    @Override
    public void ejecutar() {
        Set<Cancion> canciones = Recital.getInstance().getCanciones();

        if (canciones.isEmpty()) {
        	System.out.println("Error: El recital no posee canciones cargadas.");
        	return;
        }
        
        for(Cancion c : canciones) {
        	contratacionService.contratarArtistas(c);
        }
        
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void deshacer() {
    	Set<Cancion> canciones = Recital.getInstance().getCanciones();

        if (canciones.isEmpty()) {
        	System.out.println("Error: El recital no posee canciones cargadas.");
        	return;
        }
        
        System.out.println("Deshacer no implementado para descontratación de recital.");
    }
}
