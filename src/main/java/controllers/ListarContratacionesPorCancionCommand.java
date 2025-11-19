package controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.Artista;
import domain.Cancion;
import domain.Contratacion;
import domain.Recital;
import domain.TipoRol;

public class ListarContratacionesPorCancionCommand implements ComandoContratacion {
	@Override
	public void ejecutar() {
	    Set<Cancion> canciones = Recital.getInstance().getCanciones();
	    double costoPorCancion, costoRecital = 0;
	    
	    if (canciones.isEmpty()) {
	        System.out.println("No hay canciones cargadas en el recital.");
	        return;
	    }
	    
	    for(Cancion c : canciones) {
	        costoPorCancion = 0;
	        
	        Map<TipoRol, List<Artista>> asignados = c.getAsignaciones();
	        
	        String estado = c.getEstado().name();
	        System.out.println("\n - " + c.getTitulo() + " [" + estado + "]");
	        
	        for(TipoRol r : asignados.keySet()) {
	            List<Artista> artistasDelRol = asignados.get(r);
	        
	            for (Artista a : artistasDelRol) {
	                Contratacion contrato = Recital.getInstance()
	                                            .obtenerContratacionPorArtistaYCancion(c, a);

	                double costoIndividual = 0.0;
	                String mensajeCosto = "";

	                if (contrato != null) {
	                    costoIndividual = contrato.getCostoFinal(); 
	                    mensajeCosto = " con un costo individual de " + costoIndividual;
	                } else {
	                    mensajeCosto = " - Contrato FALTANTE (Costo no calculado)"; 
	                }

	                costoPorCancion += costoIndividual;
	                System.out.println("   > " + r + " asignado a " + 
	                                   a.getNombre() + 
	                                   mensajeCosto);
	            }
	        }
	        
	        System.out.println("______________________________");
	        System.out.println("\n Costo Por Cancion " + costoPorCancion);
	        
	        costoRecital += costoPorCancion;
	    }
	    System.out.println("______________________________");
	    System.out.println("\n Costo Total del recital " + costoRecital);
	    System.out.println("-------------------------------------------");
	}

    @Override
    public void deshacer() {
    }
}
