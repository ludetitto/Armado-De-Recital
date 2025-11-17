package services;

import java.util.*;

import domain.Cancion;
import domain.TipoRol;

public class CancionService {

    public List<TipoRol> verRoles(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser null");
        }
        List<TipoRol> roles = new ArrayList<>();
        cancion.getAsignaciones().forEach((rol, artistas) -> roles.add(rol));
        return roles;
    }

    public Map<TipoRol, Integer> verRolesFaltantes(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser null");
        }

        Map<TipoRol, Integer> faltantes = new LinkedHashMap<>();

        for (Map.Entry<TipoRol, Integer> entry : cancion.getRolesRequeridos().entrySet()) {
            faltantes.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        for (TipoRol rolCubierto : verRoles(cancion)) {
            faltantes.computeIfPresent(rolCubierto, (k, v) -> v > 1 ? v - 1 : null);
        }

        return faltantes;
    }
}
