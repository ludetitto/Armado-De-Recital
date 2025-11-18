package controllers;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import services.ArtistaService;
import services.ContratacionService;

import java.util.*;

public class ContratarArtistasParaCancionCommand implements ComandoContratacion {

    private final String tituloCancion;
    private ContratacionService contratacionService;
    private final Map<TipoRol, List<Artista>> asignadosEnEstaEjecucion = new EnumMap<>(TipoRol.class);

    public ContratarArtistasParaCancionCommand(ArtistaService artistaService,
                                               String tituloCancion) {
        contratacionService = new ContratacionService(artistaService);
        this.tituloCancion = tituloCancion;
    }

    @Override
    public void ejecutar() {
        Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);

        contratacionService.contratarArtistas(cancion);
        
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void deshacer() {
        Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);
        
        if (cancion == null) 
        	return;

        asignadosEnEstaEjecucion.forEach((rol, lista) -> {
            for (Artista a : lista) {
                cancion.desasignarArtista(a, rol);
            }
        });
        
        asignadosEnEstaEjecucion.clear();
        System.out.println("Deshacer: se revirtieron las asignaciones nuevas en '" + tituloCancion + "'");
    }
}
