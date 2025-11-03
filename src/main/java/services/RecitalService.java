package services;

import domain.Artista;
import domain.Cancion;
import domain.Contratacion;
import domain.Recital;
import domain.TipoRol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecitalService {
	
	Map<TipoRol, Integer> verRolesFaltantes(Recital recital) {
		return recital.getRolesFaltantesTotal();
	}
	
	List<Artista> verArtistasContratados(Recital recital) {
		List<Contratacion> contrataciones = recital.getContrataciones();
		return contrataciones.stream()
			.map(Contratacion::getArtista)
			.toList();
	}
	
	Set<Cancion> verCanciones(Recital recital) {
		return recital.getCanciones();
	}
	
	public void cargarEstadoInicial(String archivo) {}
}