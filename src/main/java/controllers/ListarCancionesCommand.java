package controllers;

import domain.Cancion;
import domain.Recital;
import domain.TipoRol;

import java.util.Map;
import java.util.Set;

public class ListarCancionesCommand implements ComandoContratacion {

    @Override
    public void ejecutar() {
        Set<Cancion> canciones = Recital.getInstance().getCanciones();
        System.out.println("--- Canciones del Recital (" + canciones.size() + ") ---");
        if (canciones.isEmpty()) {
            System.out.println("No hay canciones cargadas en el recital.");
        } else {
            for (Cancion c : canciones) {
                Map<TipoRol, Integer> faltantes = c.getRolesFaltantes();
                String estado = c.getEstado().name();
                System.out.println("\n - " + c.getTitulo() + " [" + estado + "]");
                if (faltantes.isEmpty()) {
                    System.out.println("   > ¡Roles cubiertos!");
                } else {
                    faltantes.forEach((rol, cant) ->
                        System.out.println("   > " + rol + ": faltan " + cant)
                    );
                }
            }
        }
        System.out.println("-------------------------------------------");
    }

    @Override
    public void deshacer() {
        // Listado: no hay nada que deshacer
    }
}
