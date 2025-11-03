package services;

import domain.Cancion;
import domain.TipoEstado;
import domain.TipoRol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CancionService {
    List<TipoRol> verRoles(Cancion cancion) {
    	Map<TipoRol, Integer> roles = cancion.getRolesRequeridos();
    	return new ArrayList<>(roles.keySet());
    }
    
    Map<TipoRol, Integer> verRolesFaltantes(Cancion cancion) {
		 return cancion.getRolesFaltantes();
    }
    
    TipoEstado verEstado(Cancion cancion) {
		return cancion.getEstado();
    }
}