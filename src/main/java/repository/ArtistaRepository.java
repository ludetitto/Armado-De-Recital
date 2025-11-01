package repository;

import java.util.Set;

import services.TipoDeArtista;
import services.TipoRecital;
import services.TipoRol;

public class ArtistaRepository {
	public String nombre;
	public Set<TipoRol> roles;
	public Set<String> bandas;
	public double costo;
	public int maxCanciones;
	public int aniosExperiencia;
	public TipoDeArtista tipoDeArtista;
	public TipoRecital tipoRecital[];

	public ArtistaRepository(String nombre, Set<TipoRol> roles, Set<String> bandas, double costo, int maxCanciones,
			int aniosExperiencia, TipoDeArtista tipoDeArtista, TipoRecital[] tipoRecital) {
		this.nombre = nombre;
		this.roles = roles;
		this.bandas = bandas;
		this.costo = costo;
		this.maxCanciones = maxCanciones;
		this.aniosExperiencia = aniosExperiencia;
		this.tipoDeArtista = tipoDeArtista;
		this.tipoRecital = tipoRecital;
	}

	public ArtistaRepository() {

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

	public int getAniosExperiencia() {
		return aniosExperiencia;
	}

	public TipoDeArtista getTipoDeArtista() {
		return tipoDeArtista;
	}

	public TipoRecital[] getTipoRecital() {
		return tipoRecital;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setRoles(Set<TipoRol> roles) {
		this.roles = roles;
	}

	public void setBandas(Set<String> bandas) {
		this.bandas = bandas;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public void setMaxCanciones(int maxCanciones) {
		this.maxCanciones = maxCanciones;
	}

	public void setAniosExperiencia(int aniosExperiencia) {
		this.aniosExperiencia = aniosExperiencia;
	}

	public void setTipoDeArtista(TipoDeArtista tipoDeArtista) {
		this.tipoDeArtista = tipoDeArtista;
	}

	public void setTipoRecital(TipoRecital[] tipoRecital) {
		this.tipoRecital = tipoRecital;
	}

}
