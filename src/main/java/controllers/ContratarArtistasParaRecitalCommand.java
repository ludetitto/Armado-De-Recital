package controllers;

import java.util.List;

import services.ArtistaService;
import services.ContratacionService;

public class ContratarArtistasParaRecitalCommand implements ComandoContratacion {
	List<ArtistaService> candidatos;
	List<ContratacionService> nuevasContrataciones;
	
	public void ejecutar() {
		// TODO: implementar
	}
	
	public void deshacer() {
		// TODO: implementar
	}
}
