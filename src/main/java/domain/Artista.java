package domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//Le dice a Jackson que use los campos (protected) para leer/escribir.
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)

public class Artista {
	protected String nombre;

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	protected EnumMap<TipoRol, EstadoRol> roles;
	protected Set<String> bandas;
	protected double costoBase;
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

	@JsonCreator
	public Artista() {
		this.roles = new EnumMap<>(TipoRol.class); // Inicializar siempre
		this.bandas = new LinkedHashSet<>();
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
	public String getNombre() {
		return nombre;
	}

	@JsonIgnore
	public Map<TipoRol, EstadoRol> getRoles() {
		return Collections.unmodifiableMap(roles);
	}

	@JsonProperty("roles")
	public Set<TipoRol> getRolesParaGuardar() {
		// Retorna solo las CLAVES (TipoRol) del EnumMap
		return roles.keySet();
	}

	public Set<String> getBandas() {
		return Collections.unmodifiableSet(bandas);
	}

	public double getCostoBase() {
		return costoBase;
	}

	// setters

	@JsonProperty("nombre")
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

//	public void setRoles(EnumMap<TipoRol, EstadoRol> roles) {
//		this.roles = roles;
//	}

	@JsonProperty("roles")
	public void setRolesParaJackson(java.util.Set<domain.TipoRol> rolesCargados) {
		// Inicializa el EnumMap y asigna EstadoRol.BASE a cada rol cargado
		this.roles = new java.util.EnumMap<>(domain.TipoRol.class);
		if (rolesCargados != null) {
			for (domain.TipoRol rol : rolesCargados) {
				// EstadoRol.BASE se guarda internamente
				this.roles.put(rol, domain.EstadoRol.BASE);
			}
		}
	}

	@JsonProperty("bandas")
	public void setBandas(Set<String> bandas) {
		this.bandas = bandas;
	}

	@JsonProperty("costoBase")
	public void setCostoBase(double costoBase) {
		this.costoBase = costoBase;
	}

	@JsonProperty("tipo")
	public void setTipo(TipoDeArtista tipo) {
		this.tipo = tipo;
	}

	public TipoDeArtista getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return String.format("%s - Roles: %s - Bandas: %s - Costo base: $%.2f", nombre, roles, bandas, costoBase);
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