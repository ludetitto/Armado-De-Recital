package services;

import domain.Artista;
import domain.EstadoRol;
import domain.TipoRol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArtistaService {

	List<TipoRol> verRoles(Artista artista) {
		List<TipoRol> roles = new ArrayList<>();
	    Map<TipoRol, EstadoRol> rolesDelArtista = artista.getRoles();

	    for (Map.Entry<TipoRol, EstadoRol> entry : rolesDelArtista.entrySet()) {
	        EstadoRol estado = entry.getValue();
	        if (estado == EstadoRol.BASE || estado == EstadoRol.ENTRENAMIENTO) {
	            roles.add(entry.getKey());
	        }
	    }
	    return roles;
	}
	
	Boolean puedeOcuparRol(Artista artista, TipoRol rol) {
		return artista.puedeOcuparRol(rol);
	}
	
	void verGrafoColaboraciones() {
		// TODO: implementar
	}
}