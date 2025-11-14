package services;

import domain.Artista;
import domain.Cancion;
import domain.Contratacion;
import domain.TipoRol;

public class ContratacionService {

	public void generarContratacion(Artista artista, Cancion cancion, TipoRol rol) {
		new Contratacion(artista, cancion, rol, obtenerCosto(artista), 0);
	}
	
	public double obtenerCosto(Artista artista) {
		Costo costo = new CostoBase(artista.getCostoBase());
		Costo costoConEntrenamientos = new CostoEntrenamiento(costo);
		return costoConEntrenamientos.obtener();
	}
}