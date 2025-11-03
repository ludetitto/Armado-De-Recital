package controllers;

import domain.Cancion;
import domain.Recital;
import domain.TipoRol;
import services.RecitalService;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set; // Necesario para iterar sobre las canciones

public class ListarCancionesCommand implements ComandoContratacion {
	
	private final RecitalService recitalService;

	public ListarCancionesCommand(RecitalService recitalService) {
		this.recitalService = recitalService;
	}
	
	@Override
	public void ejecutar() {
		
        Recital recital = Recital.getInstance();
        Set<Cancion> canciones = recital.getCanciones(); // ⬅️ Obtenemos todas las canciones

        if (canciones.isEmpty()) {
            System.out.println("El recital aún no tiene canciones cargadas.");
            return;
        }
        
        // El servicio aún se usa para calcular el total y decidir si mostrar el detalle
        Map<TipoRol, Integer> rolesFaltantesTotal = recitalService.verRolesFaltantes(recital);

		System.out.println("--- Roles Faltantes por Canción (" + recital.getTitulo() + ") ---");

        if (rolesFaltantesTotal.isEmpty()) {
            System.out.println("¡Todas las canciones del Recital tienen sus roles cubiertos!");
            System.out.println("---------------------------------------------------------------");
            return;
        }

        // Listar el detalle por Canción
        for (Cancion cancion : canciones) {
            Map<TipoRol, Integer> faltantesCancion = cancion.getRolesFaltantes();

            System.out.println("\n[" + cancion.getTitulo() + "]");

            if (faltantesCancion.isEmpty()) {
                System.out.println(" > ¡Roles cubiertos! ✅");
            } else {
                for (Entry<TipoRol, Integer> entry : faltantesCancion.entrySet()) {
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
		// TODO Auto-generated method stub
		
	}
	

}