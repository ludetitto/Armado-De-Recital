package controllers;

import java.util.List;

import services.ArtistaService;
import services.CancionService;
import services.ContratacionService;

public class DescontratarArtistaDeCancionCommand implements ComandoContratacion {
	CancionService cancion;
	ArtistaService artista;
	List<ContratacionService> removidas;
	
	public void ejecutar() {
		// TODO: implementar
	}
	
	public void deshacer() {
		// TODO: implementar
	}
}
