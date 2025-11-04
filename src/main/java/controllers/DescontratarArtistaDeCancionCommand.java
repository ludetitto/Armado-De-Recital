package controllers;

import domain.Artista;
import domain.Cancion;
import domain.Recital;
import domain.TipoRol;

public class DescontratarArtistaDeCancionCommand implements ComandoContratacion {

    private final String tituloCancion;
    private final String nombreArtista;
    private final TipoRol rol;

    public DescontratarArtistaDeCancionCommand(String tituloCancion, String nombreArtista, TipoRol rol) {
        this.tituloCancion = tituloCancion;
        this.nombreArtista = nombreArtista;
        this.rol = rol;
    }

    @Override
    public void ejecutar() {
        Cancion cancion = Recital.getInstance().getCanciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
                .findFirst().orElse(null);
        if (cancion == null) {
            System.out.println("Error: canción no encontrada: " + tituloCancion);
            return;
        }
        Artista artista = Recital.getInstance().getArtistas().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);

        if (artista == null) {
            System.out.println("Error: artista no encontrado: " + nombreArtista);
            return;
        }

        cancion.desasignarArtista(artista, rol);
        System.out.println("Desasignado " + artista.getNombre() + " del rol " + rol + " en '" + tituloCancion + "'");
    }

    @Override
    public void deshacer() {
        // Podríamos intentar reasignar, pero requeriría guardar estado previo.
        System.out.println("Deshacer no implementado para descontratación puntual.");
    }
}
