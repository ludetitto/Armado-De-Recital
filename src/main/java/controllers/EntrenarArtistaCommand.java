package controllers;

import domain.Artista;
import domain.Recital;
import domain.TipoRol;

public class EntrenarArtistaCommand implements ComandoContratacion {

    private final String nombreArtista;
    private TipoRol rolAgregado = null;

    // Firma que usa tu Menu (solo String)
    public EntrenarArtistaCommand(String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    // Sobrecarga compatible con tu variante de servicios
    public EntrenarArtistaCommand(services.PrologService ps, services.ArtistaService as, String nombreArtista) {
        this.nombreArtista = nombreArtista;
    }

    @Override
    public void ejecutar() {
        Artista artista = Recital.getInstance().getArtistas().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);

        if (artista == null) {
            System.out.println("Error: artista no encontrado: " + nombreArtista);
            return;
        }

        for (TipoRol r : TipoRol.values()) {
            if (!artista.puedeOcuparRol(r)) {
                artista.agregarRol(r);
                rolAgregado = r;
                System.out.println("Entrenamiento aplicado: " + artista.getNombre() + " ahora puede " + r);
                return;
            }
        }
        System.out.println("No se encontró un rol nuevo para entrenar a " + artista.getNombre());
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
