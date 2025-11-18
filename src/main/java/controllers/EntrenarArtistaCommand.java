package controllers;

import domain.Artista;
import domain.TipoRol;
import services.ArtistaService;

public class EntrenarArtistaCommand implements ComandoContratacion {

    private final String nombreArtista;
    private ArtistaService artistaService;
    private TipoRol rolAgregado;

    public EntrenarArtistaCommand(ArtistaService artistaService, String nombreArtista, String rolAgregado) {
        this.nombreArtista = nombreArtista;
        this.rolAgregado = TipoRol.valueOf(rolAgregado);
        this.artistaService = artistaService;
    }

    public EntrenarArtistaCommand(services.PrologService ps, services.ArtistaService as, String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    @Override
    public void ejecutar() {
        Artista artista = artistaService.getArtistasCandidatos().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);

        if (artista == null) {
           throw new IllegalArgumentException("Error: artista no encontrado: " + nombreArtista);
        }
        
        artista.entrenarEn(rolAgregado);
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
