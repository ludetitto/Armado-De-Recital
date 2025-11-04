package controllers;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import services.ArtistaService;
import services.CancionService;

import java.util.*;

public class ContratarArtistasParaCancionCommand implements ComandoContratacion {

    private final String tituloCancion;

    @SuppressWarnings("unused")
    private final ArtistaService artistaServiceOpt; // puede ser null
    private final CancionService cancionServiceOpt; // puede ser null

    private final Map<TipoRol, List<Artista>> asignadosEnEstaEjecucion = new EnumMap<>(TipoRol.class);

    public ContratarArtistasParaCancionCommand(String tituloCancion) {
        this(null, null, tituloCancion);
    }

    public ContratarArtistasParaCancionCommand(ArtistaService artistaService,
                                               CancionService cancionService,
                                               String tituloCancion) {
        this.artistaServiceOpt = artistaService;
        this.cancionServiceOpt = cancionService;
        this.tituloCancion = tituloCancion;
    }

    @Override
    public void ejecutar() {
        Recital recital = Recital.getInstance();

        Cancion cancion = recital.getCanciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
                .findFirst().orElse(null);

        if (cancion == null) {
            System.out.println("Error: Canción '" + tituloCancion + "' no encontrada.");
            return;
        }

        CancionService cancionService = (cancionServiceOpt != null) ? cancionServiceOpt : new CancionService();

        Map<TipoRol, Integer> faltantes = new LinkedHashMap<>(cancionService.verRolesFaltantes(cancion));
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

        Map<TipoRol, Integer> remanente = cancionService.verRolesFaltantes(cancion);
        if (remanente.isEmpty()) {
            System.out.println("Resultado: ¡Roles cubiertos!");
        } else {
            System.out.println("Resultado: aún faltan roles:");
            remanente.forEach((r, n) -> System.out.println(" - " + r + ": " + n));
        }
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void deshacer() {
        Recital recital = Recital.getInstance();
        Cancion cancion = recital.getCanciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
                .findFirst().orElse(null);
        if (cancion == null) return;

        asignadosEnEstaEjecucion.forEach((rol, lista) -> {
            for (Artista a : lista) {
                cancion.desasignarArtista(a, rol);
            }
        });
        asignadosEnEstaEjecucion.clear();
        System.out.println("Deshacer: se revirtieron las asignaciones nuevas en '" + tituloCancion + "'");
    }
}
