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
		List<Artista> artistasContratados = new ArrayList<>();
		for(Contratacion c : contrataciones) {
			artistasContratados.add(c.getArtista());
		}
		
		return artistasContratados;
	}
	
	Set<Cancion> verCanciones(Recital recital) {
		return recital.getCanciones();
	}
	
	public void cargarEstadoInicial(String archivo) {}
}