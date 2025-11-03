package domain;

import java.util.Set;

public class ArtistaExterno extends Artista {

    private final int maxCanciones;

    public ArtistaExterno(String nombre, Set<TipoRol> roles, Set<String> bandas, double costoBase, int maxCanciones) {
        super(nombre, TipoDeArtista.EXTERNO, roles, bandas, costoBase);
        this.maxCanciones = maxCanciones;
    }

    public int getMaxCanciones() {
        return maxCanciones;
    }
}
