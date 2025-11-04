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
		if (cancion == null) {
			throw new IllegalArgumentException("cancion no puede ser null");
		}
    
		return List.copyOf(cancion.getRolesRequeridos().keySet());
	}
    
	Map<TipoRol, Integer> verRolesFaltantes(Cancion cancion) {
		if (cancion == null) {
			throw new IllegalArgumentException("cancion no puede ser null");
		}

		return new EnumMap<>(cancion.getRolesFaltantes());
	}
}