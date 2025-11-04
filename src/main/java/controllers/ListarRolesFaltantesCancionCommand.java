package controllers;

import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import services.CancionService; 

import java.util.Map;
import java.util.Map.Entry;

public class ListarRolesFaltantesCancionCommand implements ComandoContratacion {
	
	private final CancionService cancionService; 
	private final String tituloCancion;

	public ListarRolesFaltantesCancionCommand(CancionService cancionService, String tituloCancion) {
		this.cancionService = cancionService;
		this.tituloCancion = tituloCancion;
	}
	
	@Override
	public void ejecutar() {
		
        Recital recital = Recital.getInstance();
        
        Cancion cancionAInspeccionar = recital.getCanciones().stream()
            .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
            .findFirst()
            .orElse(null);
        
        if (cancionAInspeccionar == null) {
            System.out.println("Error: Canción '" + tituloCancion + "' no encontrada en el Recital.");
            return;
        }

        Map<TipoRol, Integer> faltantesCancion = cancionService.verRolesFaltantes(cancionAInspeccionar);
        
        System.out.println("\n--- Roles Faltantes para: " + cancionAInspeccionar.getTitulo() + " ---");

        if (faltantesCancion.isEmpty()) {
            System.out.println("¡Roles cubiertos! La canción está lista para tocarse."); 
        } else {
            for (Entry<TipoRol, Integer> entry : faltantesCancion.entrySet()) {
                TipoRol rol = entry.getKey();
                Integer cantidad = entry.getValue();
                System.out.println(" > " + rol + ": Faltan " + cantidad);
            }
        }
		System.out.println("----------------------------------------------------------");
	}

	@Override
	public void deshacer() {
		// La acción de listar (consultar) es de solo lectura y no requiere deshacer.
	}
}