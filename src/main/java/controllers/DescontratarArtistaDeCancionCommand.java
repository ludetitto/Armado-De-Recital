package controllers;

import domain.TipoRol;
import services.RecitalService;

// BONUS: Arrepentimiento
public class DescontratarArtistaDeCancionCommand implements ComandoContratacion {

    private final String tituloCancion;
    private final String nombreArtista;
    private final TipoRol rol;
    private final RecitalService recitalService;
    
    public DescontratarArtistaDeCancionCommand(String tituloCancion, String nombreArtista, TipoRol rol, RecitalService recitalService) {
        this.tituloCancion = tituloCancion;
        this.nombreArtista = nombreArtista;
        this.rol = rol;
        this.recitalService = recitalService;
    }

    @Override
    public void ejecutar() {
        recitalService.designarArtistaDeCancion(nombreArtista, tituloCancion, rol);
        recitalService.descontratar(nombreArtista, tituloCancion);
        
        System.out.println("Desasignado " + nombreArtista + " de '" + tituloCancion + "'");

    }

    @Override
    public void deshacer() {
        System.out.println("Deshacer no implementado para descontratación puntual.");
    }
}
