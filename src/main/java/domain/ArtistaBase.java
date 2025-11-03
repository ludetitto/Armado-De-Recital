package domain;

import java.util.Set;

public class ArtistaBase extends Artista {

    public ArtistaBase(String nombre, Set<TipoRol> roles, Set<String> bandas) {
        super(nombre, TipoDeArtista.BASE, roles, bandas, 0.0);
    }
}
