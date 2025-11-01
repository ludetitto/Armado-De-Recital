package repository;

import java.util.Set;

import services.TipoRol;

public class ArtistaRepository {
    public String nombre;
    public Set<TipoRol> roles;
    public Set<String> bandas;
    public double costo;
    public int maxCanciones;
    
	public ArtistaRepository(String nombre, Set<TipoRol> roles, Set<String> bandas, double costo, int maxCanciones) {
		super();
		this.nombre = nombre;
		this.roles = roles;
		this.bandas = bandas;
		this.costo = costo;
		this.maxCanciones = maxCanciones;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Set<TipoRol> getRoles() {
		return roles;
	}
	
	public Set<String> getBandas() {
		return bandas;
	}
	
	public double getCosto() {
		return costo;
	}
	
	public int getMaxCanciones() {
		return maxCanciones;
	}
    
}
