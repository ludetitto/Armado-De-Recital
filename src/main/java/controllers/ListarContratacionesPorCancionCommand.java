package controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;

public class ListarContratacionesPorCancionCommand implements ComandoContratacion {
	@Override
    public void ejecutar() {
        Set<Cancion> canciones = Recital.getInstance().getCanciones();
        
        if (canciones.isEmpty()) {
            System.out.println("No hay canciones cargadas en el recital.");
        }
        
        for(Cancion c : canciones) {
            Map<TipoRol, List<Artista>> asignados = c.getAsignaciones();
            
            String estado = c.getEstado().name();
            System.out.println("\n - " + c.getTitulo() + " [" + estado + "]");
            
            for(TipoRol r : asignados.keySet()) {
                List<Artista> artistasDelRol = asignados.get(r);
            
	            for (Artista a : artistasDelRol) {
	                double costoIndividual = Recital.getInstance()
	                                            .obtenerContratacionPorArtistaYCancion(c, a).getCostoFinal();
	
	                System.out.println("   > " + r + " asignado a " + 
	                                   a.getNombre() + 
	                                   " con un costo individual de " + 
	                                   costoIndividual);
	            }
            }
        }
        System.out.println("-------------------------------------------");
    }

    @Override
    public void deshacer() {
    }
}
