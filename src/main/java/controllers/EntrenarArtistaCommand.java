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
    
    @Override
    public void ejecutar() {
        Artista artista = Recital.getInstance().obtenerArtistaPorNombre(nombreArtista);

        if (artista == null) {
           System.out.println("Error: artista no encontrado: " + nombreArtista);
        }
        
        if(artista.entrenarEn(rolAgregado))
        	System.out.println("El artista " + artista.getNombre() + " ahora posee el rol " + rolAgregado);
        else
        	System.out.println("El artista " + artista.getNombre() + " YA POSEE el rol " + rolAgregado);
    }

    @Override
    public void deshacer() {
        System.out.println("Atención: no es posible revertir el entrenamiento.");
    }
}
