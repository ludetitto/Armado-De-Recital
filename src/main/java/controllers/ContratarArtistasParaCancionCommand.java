package controllers;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import services.ArtistaService;
import services.CancionService;
import services.ContratacionService;

import java.util.*;

public class ContratarArtistasParaCancionCommand implements ComandoContratacion {

    private final String tituloCancion;
    private ContratacionService contratacionService;

    private final Map<TipoRol, List<Artista>> asignadosEnEstaEjecucion = new EnumMap<>(TipoRol.class);

    public ContratarArtistasParaCancionCommand(String tituloCancion) {
        this(null, null, tituloCancion);
    }

    public ContratarArtistasParaCancionCommand(ArtistaService artistaService,
                                               CancionService cancionService,
                                               String tituloCancion) {
        contratacionService = new ContratacionService(cancionService, artistaService);
        this.tituloCancion = tituloCancion;
    }

    @Override
    public void ejecutar() {
        Recital recital = Recital.getInstance();

        Cancion cancion = recital.getCanciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
                .findFirst().orElse(null);

        contratacionService.contratarArtistas(cancion);
        
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
