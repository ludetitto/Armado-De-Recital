package controllers;

import java.util.List;

import services.ArtistaService;
import services.CancionService;
import services.ContratacionService;

public class ContratarArtistasParaCancionCommand implements ComandoContratacion {
	CancionService cancion;
	List<ArtistaService> candidatos;
	List<ContratacionService> nuevasContrataciones;
	
	public void ejecutar() {
		// TODO: implementar
	}
	
	public void deshacer() {
		// TODO: implementar
	}
}
