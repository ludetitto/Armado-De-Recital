package controllers;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;

public class DescontratarArtistaDeRecitalCommand implements ComandoContratacion {

    private final String nombreArtista;
    private final TipoRol rol; // si es null, lo quita de todos los roles

    public DescontratarArtistaDeRecitalCommand(String nombreArtista, TipoRol rol) {
        this.nombreArtista = nombreArtista;
        this.rol = rol;
    }

    @Override
    public void ejecutar() {
        Recital recital = Recital.getInstance();
        Artista artista = recital.getArtistas().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);
        if (artista == null) {
            System.out.println("Error: artista no encontrado: " + nombreArtista);
            return;
        }

        int removidas = 0;
        for (Cancion c : recital.getCanciones()) {
            if (rol == null) {
                // quitar de todos los roles donde esté
                for (var entry : c.getAsignaciones().entrySet()) {
                    if (entry.getValue().contains(artista)) {
                        c.desasignarArtista(artista, entry.getKey());
                        removidas++;
                    }
                }
            } else {
                if (c.getAsignaciones().getOrDefault(rol, java.util.List.of()).contains(artista)) {
                    c.desasignarArtista(artista, rol);
                    removidas++;
                }
            }
        }
        System.out.println("Desasignaciones realizadas: " + removidas);
    }

    @Override
    public void deshacer() {
        System.out.println("Deshacer no implementado para descontratación global.");
    }
}
