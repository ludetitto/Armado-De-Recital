package controllers;

import domain.Artista;
import domain.Recital;
import domain.TipoRol;

public class EntrenarArtistaCommand implements ComandoContratacion {

    private final String nombreArtista;
    private TipoRol rolAgregado;

    public EntrenarArtistaCommand(String nombreArtista, String rolAgregado) {
        this.nombreArtista = nombreArtista;
        this.rolAgregado = TipoRol.valueOf(rolAgregado);
    }

    public EntrenarArtistaCommand(services.PrologService ps, services.ArtistaService as, String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    @Override
    public void ejecutar() {
        Artista artista = Recital.getInstance().obtenerArtistaPorNombre(nombreArtista);

        if (artista == null) {
           throw new IllegalArgumentException("Error: artista no encontrado: " + nombreArtista);
        }
        
        if(artista.entrenarEn(rolAgregado))
        	System.out.println("El artistas " + artista.getNombre() + " ahora posee el rol " + rolAgregado);
        else
        	System.out.println("El artistas " + artista.getNombre() + " YA POSEE el rol " + rolAgregado);
    }

    @Override
    public void deshacer() {
        if (rolAgregado == null) {
            System.out.println("Nada para deshacer.");
            return;
        }
        System.out.println("Atención: no es posible revertir el entrenamiento (no existe removerRol en dominio).");
    }
}
