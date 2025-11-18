package controllers;

import domain.Cancion;
import domain.Recital;
import domain.TipoRol;

import java.util.Map;
import java.util.Map.Entry;

public class ListarRolesFaltantesCancionCommand implements ComandoContratacion {
	
	private final String tituloCancion;

	public ListarRolesFaltantesCancionCommand(String tituloCancion) {
		this.tituloCancion = tituloCancion;
	}
	
	@Override
	public void ejecutar() {
        Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);
        
        if (cancion == null) {
            System.out.println("Error: Canción '" + tituloCancion + "' no encontrada en el Recital.");
            return;
        }
        
        Map<TipoRol, Integer> rolesFaltantes = cancion.getRolesFaltantes();
        
        if (rolesFaltantes.isEmpty()) {
            System.out.println("¡Roles cubiertos! La canción está lista para tocarse."); 
        } else {
            for (Entry<TipoRol, Integer> entry : rolesFaltantes.entrySet()) {
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