package controllers;

import services.ArtistaService;
import services.CancionService;
import services.ContratacionService;

public class ContratarArtistasParaRecitalCommand implements ComandoContratacion {

	private ContratacionService contratacionService;

    public ContratarArtistasParaRecitalCommand() {
        this(null, null);
    }

    public ContratarArtistasParaRecitalCommand(ArtistaService artistaService, CancionService cancionService) {
    	contratacionService = new ContratacionService(cancionService, artistaService);
    }

    @Override
    public void ejecutar() {
        contratacionService.contratarArtistasRecital();
        
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void deshacer() {
        System.out.println("Deshacer general no implementado para contratacion de todo el recital.");
    }
}
