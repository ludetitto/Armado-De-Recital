package controllers;

import domain.Cancion;
import domain.Recital;
import domain.TipoRol;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set; // Necesario para iterar sobre las canciones

public class  ListarRolesFaltantesRecitalCommand implements ComandoContratacion {

	@Override
	public void ejecutar() {
		
        Set<Cancion> canciones = Recital.getInstance().getCanciones();

        if (canciones.isEmpty()) {
            System.out.println("Error: El recital aún no tiene canciones cargadas.");
            return;
        }
        
        for(Cancion c : canciones) {
        	Map<TipoRol, Integer> rolesFaltantes = c.getRolesFaltantes();
            
    		System.out.println("--- Roles Faltantes por Canción (" + c.getTitulo() + ") ---");

            if (rolesFaltantes.isEmpty()) {
                System.out.println("¡Roles cubiertos! La canción está lista para tocarse."); 
            } else {
                for (Entry<TipoRol, Integer> entry : rolesFaltantes.entrySet()) {
                    TipoRol rol = entry.getKey();
                    Integer cantidad = entry.getValue();
                    System.out.println(" > " + rol + ": Faltan " + cantidad);
                }
            }
        }

		System.out.println("\n---------------------------------------------------------------");
	}

	@Override
	public void deshacer() {
	}
}