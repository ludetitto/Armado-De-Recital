package domain;

import java.util.*;

public abstract class Artista {
    protected String nombre;
    protected Set<TipoRol> roles;
    protected Set<String> bandas;
    protected double costoBase;
    
    public Artista(String nombre, Set<TipoRol> roles, Set<String> bandas, double costoBase) {
        this.nombre = nombre;
        this.roles = new LinkedHashSet<>(roles);
        this.bandas = new LinkedHashSet<>(bandas);
        this.costoBase = costoBase;
    }
    
    // Métodos abstractos que implementarán las subclases
    public abstract double calcularCosto();
    public abstract boolean esBase();
    
    // Verifica si el artista puede ocupar un rol
    public boolean puedeOcuparRol(TipoRol rol) {
        return roles.contains(rol);
    }
    
    // Agregar nuevo rol (entrenamiento)
    public void agregarRol(TipoRol nuevoRol) {
        if (!roles.contains(nuevoRol)) {
            roles.add(nuevoRol);
            this.costoBase *= 1.5; // +50% por entrenamiento
        }
    }
    
    // Verifica si compartió banda con otro artista
    public boolean compartioBanda(Artista otro) {
        return this.bandas.stream().anyMatch(otro.bandas::contains);
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public Set<TipoRol> getRoles() { return Collections.unmodifiableSet(roles); }
    public Set<String> getBandas() { return Collections.unmodifiableSet(bandas); }
    public double getCostoBase() { return costoBase; }
    
    @Override
    public String toString() {
        return String.format("%s - Roles: %s - Bandas: %s - Costo: $%.2f", 
            nombre, roles, bandas, calcularCosto());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artista artista = (Artista) o;
        return nombre.equals(artista.nombre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}