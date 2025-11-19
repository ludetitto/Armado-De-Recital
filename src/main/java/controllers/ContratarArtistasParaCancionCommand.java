package controllers;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import services.ContratacionService;

import java.util.*;

public class ContratarArtistasParaCancionCommand implements ComandoContratacion {

    private final String tituloCancion;
    private final ContratacionService contratacionService;
    private final Map<TipoRol, List<Artista>> asignadosEnEstaEjecucion = new EnumMap<>(TipoRol.class);

    public ContratarArtistasParaCancionCommand(String tituloCancion) {
        contratacionService = new ContratacionService();
        this.tituloCancion = tituloCancion;
    }

    @Override
    public void ejecutar() {
        Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);

        if (cancion == null) {
        	System.out.println("Error: La canción solicitada no existe en el recital.");
        	return;
        }

        
        contratacionService.contratarArtistas(cancion);
        
        System.out.println("-------------------------------------------------------------");
    }

    @Override
    public void deshacer() {
        Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);
        
        if (cancion == null) {
        	System.out.println("Error: La canción solicitada no existe en el recital.");
        	return;
        }

        asignadosEnEstaEjecucion.forEach((rol, lista) -> {
            for (Artista a : lista) {
                cancion.desasignarArtista(a, rol);
                Recital.getInstance().eliminarContratacion(Recital.getInstance().obtenerContratacionPorArtistaYCancion(cancion, a));
            }
        });
        
        asignadosEnEstaEjecucion.clear();
        System.out.println("Deshacer: se revirtieron las asignaciones nuevas en '" + tituloCancion + "'");
    }
}
