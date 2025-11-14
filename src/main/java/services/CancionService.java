package services;

import java.util.*;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import repository.CancionRepository;

public class CancionService {

    @SuppressWarnings("unused")
    private final CancionRepository repo;

    public CancionService(CancionRepository repo) {
        this.repo = repo;
    }

    public CancionService() {
        this(new CancionRepository());
    }

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

	public void contratarArtistas(Cancion cancion) {
		Recital recital = Recital.getInstance();
		
		Map<TipoRol, Integer> faltantes = new LinkedHashMap<>(verRolesFaltantes(cancion));
        if (faltantes.isEmpty()) {
            System.out.println("La canción '" + cancion.getTitulo() + "' ya tiene todos los roles cubiertos.");
            return;
        }

		List<Artista> candidatos = new ArrayList<>(recital.getArtistas());
        Map<TipoRol, List<Artista>> actuales = cancion.getAsignaciones();

        System.out.println("--- Contratación automática para '" + cancion.getTitulo() + "' ---");

        for (Map.Entry<TipoRol, Integer> e : faltantes.entrySet()) {
            TipoRol rol = e.getKey();
            int necesarios = e.getValue();
            int cubiertos = 0;

            List<Artista> ya = actuales.getOrDefault(rol, List.of());

            for (Artista a : candidatos) {
                if (cubiertos >= necesarios) break;
                if (a.puedeOcuparRol(rol) && !ya.contains(a)) {
                    try {
                        cancion.asignarArtista(a, rol);
                        HashMap<TipoRol, List<Artista>> asignadosEnEstaEjecucion = new HashMap<TipoRol, List<Artista>>();
						asignadosEnEstaEjecucion.computeIfAbsent(rol, k -> new ArrayList<>()).add(a);
                        cubiertos++;
                        System.out.println(" + Asignado: " + a.getNombre() + " -> " + rol);
                    } catch (IllegalArgumentException ex) {
                        // continuar
                    }
                }
            }

            if (cubiertos < necesarios) {
                System.out.println(" ! No se pudo cubrir completamente " + rol + " (faltaron "
                        + (necesarios - cubiertos) + ")");
            }
        }

        Map<TipoRol, Integer> remanente = verRolesFaltantes(cancion);
        if (remanente.isEmpty()) {
            System.out.println("Resultado: ¡Roles cubiertos!");
        } else {
            System.out.println("Resultado: aún faltan roles:");
            remanente.forEach((r, n) -> System.out.println(" - " + r + ": " + n));
        }
	}
}
