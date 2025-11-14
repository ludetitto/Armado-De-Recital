package controllers;

import domain.Artista;
import domain.Recital;
import domain.TipoRol;
import services.ArtistaService;

public class EntrenarArtistaCommand implements ComandoContratacion {

    private final String nombreArtista;
    private final ArtistaService artistaService = new ArtistaService();
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
        Artista artista = Recital.getInstance().getArtistasCandidatos().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);

        if (artista == null) {
           throw new IllegalArgumentException("Error: artista no encontrado: " + nombreArtista);
        }
        
        artistaService.recibirEntrenamiento(artista, rolAgregado);
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
