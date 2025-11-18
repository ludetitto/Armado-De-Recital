package controllers;

import domain.TipoRol;
import services.RecitalService;


public class DescontratarArtistaDeCancionCommand implements ComandoContratacion {

    private final String tituloCancion;
    private final String nombreArtista;
    private final TipoRol rol;
    private final RecitalService recitalService;
    
    public DescontratarArtistaDeCancionCommand(String tituloCancion, String nombreArtista, TipoRol rol, RecitalService recitalService) {
        this.tituloCancion = tituloCancion;
        this.nombreArtista = nombreArtista;
        this.rol = rol;
        this.recitalService =recitalService;
    }

    @Override
    public void ejecutar() {
        
        
        recitalService.designarArtistaDeCancion(nombreArtista, tituloCancion, rol);
        
        recitalService.descontratar(nombreArtista);
        
        System.out.println("Desasignado " + nombreArtista + " de '" + tituloCancion + "'");

    }
    
    

    @Override
    public void deshacer() {
        // Podríamos intentar reasignar, pero requeriría guardar estado previo.
        System.out.println("Deshacer no implementado para descontratación puntual.");
    }
}
