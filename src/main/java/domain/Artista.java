package domain;

import java.util.*;

public abstract class Artista {
	protected String nombre;
    protected EnumMap<TipoRol, EstadoRol> roles;
    protected Set<String> bandas;
    protected final double costoBase;
    protected TipoDeArtista tipo = TipoDeArtista.BASE;
    
    public Artista(String nombre, TipoDeArtista tipo, Set<TipoRol> roles, Set<String> bandas, double costoBase) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.roles = new EnumMap<>(TipoRol.class);
        
        if (roles != null) {
            for (TipoRol rol : roles) {
                this.roles.put(rol, EstadoRol.BASE);
            }
        }
        
        this.bandas = new LinkedHashSet<>(bandas);
        this.costoBase = costoBase;
    }
    
    public boolean esBase() {
    	return tipo == TipoDeArtista.BASE;
    }
    
    // Verifica si el artista puede ocupar un rol
    public boolean puedeOcuparRol(TipoRol rol) {
        return roles.get(rol) == EstadoRol.BASE || roles.get(rol) == EstadoRol.BASE;
    }
    
    // Agregar nuevo rol (entrenamiento)
    public void agregarRol(TipoRol nuevoRol) {
        if (roles.get(nuevoRol) == null) {
            roles.put(nuevoRol, EstadoRol.ENTRENAMIENTO);
        }
    }
    
    // Verifica si compartió banda con otro artista
    public boolean compartioBanda(Artista otro) {
        return this.bandas.stream().anyMatch(otro.bandas::contains);
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public Map<TipoRol, EstadoRol> getRoles() { return Collections.unmodifiableMap(roles); }
    public Set<String> getBandas() { return Collections.unmodifiableSet(bandas); }
    public double getCostoBase() { return costoBase; }
    
    @Override
    public String toString() {
        return String.format("%s - Roles: %s - Bandas: %s - Costo base: $%.2f", 
            nombre, roles, bandas, costoBase);
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