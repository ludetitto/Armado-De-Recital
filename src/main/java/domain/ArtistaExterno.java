package domain;

import java.util.Set;

public class ArtistaExterno extends Artista {
    private int maxCanciones;
    private int cancionesAsignadas;
    
    public ArtistaExterno(String nombre, Set<TipoRol> roles, Set<String> bandas, 
                          double costo, int maxCanciones) {
        super(nombre, roles, bandas, costo);
        this.maxCanciones = maxCanciones;
        this.cancionesAsignadas = 0;
    }
    
    @Override
    public double calcularCosto() {
        return costoBase;
    }
    
    @Override
    public boolean esBase() {
        return false;
    }
    
    public boolean puedeTocarMasCanciones() {
        return cancionesAsignadas < maxCanciones;
    }
    
    public void asignarCancion() {
        if (!puedeTocarMasCanciones()) {
            throw new IllegalStateException(
                "El artista " + nombre + " ya alcanzó su máximo de canciones (" + maxCanciones + ")"
            );
        }
        cancionesAsignadas++;
    }
    
    public void desasignarCancion() {
        if (cancionesAsignadas > 0) {
            cancionesAsignadas--;
        }
    }
    
    // Getters
    public int getMaxCanciones() { return maxCanciones; }
    public int getCancionesAsignadas() { return cancionesAsignadas; }
    
    @Override
    public String toString() {
        return String.format("[EXTERNO] %s - Roles: %s - Costo: $%.2f - Canciones: %d/%d", 
            nombre, getRoles(), calcularCosto(), cancionesAsignadas, maxCanciones);
    }
}