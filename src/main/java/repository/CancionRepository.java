package repository;

import java.util.List;
import java.util.Map;

import services.ArtistaService;
import services.TipoEstado;
import services.TipoRol;

public class CancionRepository {
	 public String nombre;
	 public TipoEstado estado;
	 int duracion;
	 private Map<TipoRol, Integer> rolesRequeridos; // para poder ver que roles faltan en X cancion?
	 private Map<TipoRol, List<ArtistaService>> asignaciones;
	 
	 public CancionRepository(String nombre, TipoEstado estado, int duracion,
			Map<TipoRol, List<ArtistaService>> asignaciones) {
		this.nombre = nombre;
		this.estado = estado;
		this.duracion = duracion;
		this.asignaciones = asignaciones;
	 }
	
		 
	 // Constructor requerido por Jackson para deserialización (es el que se usa si no hay @JsonCreator)
	 public CancionRepository() {
		 // Inicializa colecciones para seguridad, aunque Jackson las sobrescribirá
		 this.rolesRequeridos = new java.util.HashMap<>();
		 this.asignaciones = new java.util.HashMap<>();
	 }


	 public String getNombre() {
		 return nombre;
	 }


	 public void setNombre(String nombre) {
		 this.nombre = nombre;
	 }


	 public TipoEstado getEstado() {
		 return estado;
	 }


	 public void setEstado(TipoEstado estado) {
		 this.estado = estado;
	 }


	 public int getDuracion() {
		 return duracion;
	 }


	 public void setDuracion(int duracion) {
		 this.duracion = duracion;
	 }


	 public Map<TipoRol, Integer> getRolesRequeridos() {
		 return rolesRequeridos;
	 }


	 public void setRolesRequeridos(Map<TipoRol, Integer> rolesRequeridos) {
		 this.rolesRequeridos = rolesRequeridos;
	 }


	 public Map<TipoRol, List<ArtistaService>> getAsignaciones() {
		 return asignaciones;
	 }


	 public void setAsignaciones(Map<TipoRol, List<ArtistaService>> asignaciones) {
		 this.asignaciones = asignaciones;
	 }
 
	 
	 
	 
}
