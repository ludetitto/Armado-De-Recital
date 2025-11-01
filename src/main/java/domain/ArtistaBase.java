package domain;

import java.util.Set;

public class ArtistaBase extends Artista {
    
    public ArtistaBase(String nombre, Set<TipoRol> roles, Set<String> bandas) {
        super(nombre, roles, bandas, 0.0); // Artistas base no tienen costo
    }
    
    @Override
    public double calcularCosto() {
        return 0.0; // Artistas base son gratis
    }
    
    @Override
    public boolean esBase() {
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("[BASE] %s - Roles: %s", nombre, getRoles());
    }
}	