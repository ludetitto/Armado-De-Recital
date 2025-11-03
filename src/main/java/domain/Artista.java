package domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

//Si no encuentra el campo 'type' en el JSON, Jackson usará ArtistaBase por defecto.
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type", defaultImpl = ArtistaBase.class 
)
@JsonSubTypes({ @JsonSubTypes.Type(value = ArtistaBase.class, name = "base"),
		@JsonSubTypes.Type(value = ArtistaExterno.class, name = "externo") })


//Forzamos a Jackson a usar los FIELDS (atributos) para leer/escribir.
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)

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

	public Artista() {
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
	public String getNombre() {
		return nombre;
	}

	public Set<TipoRol> getRoles() {
		return Collections.unmodifiableSet(roles);
	}

	public Set<String> getBandas() {
		return Collections.unmodifiableSet(bandas);
	}

	public double getCostoBase() {
		return costoBase;
	}

	@Override
	public String toString() {
		return String.format("%s - Roles: %s - Bandas: %s - Costo: $%.2f", nombre, roles, bandas, calcularCosto());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Artista artista = (Artista) o;
		return nombre.equals(artista.nombre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}
}