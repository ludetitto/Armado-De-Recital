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

        for(Cancion c : canciones) {
        	contratacionService.contratarArtistas(c);
        }
        
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void deshacer() {
        System.out.println("Deshacer general no implementado para contratacion de todo el recital.");
    }
}
