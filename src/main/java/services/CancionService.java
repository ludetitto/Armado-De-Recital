package services;

import domain.Cancion;
import domain.TipoRol;
import repository.CancionRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CancionService {
	private final CancionRepository repo;

	public CancionService(CancionRepository repo) {
		this.repo = repo;
	}

	List<TipoRol> verRoles(Cancion cancion) {
		// Devuelve la lista de roles requeridos por la canción recibida.
		if (cancion == null) {
			throw new IllegalArgumentException("cancion no puede ser null");
		}
		// Mantener el orden natural de los enums usando la clave del EnumMap
		return List.copyOf(cancion.getRolesRequeridos().keySet());
	}
    
	Map<TipoRol, Integer> verRolesFaltantes(Cancion cancion) {
		// Para la canción recibida, devuelve el mapa de roles faltantes
		if (cancion == null) {
			throw new IllegalArgumentException("cancion no puede ser null");
		}

		// getRolesFaltantes ya calcula y devuelve sólo los roles con cantidad > 0
		return new EnumMap<>(cancion.getRolesFaltantes());
	}
}