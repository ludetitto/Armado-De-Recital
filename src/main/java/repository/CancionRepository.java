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
	 // public Map<TipoRol, Integer> rolesRequeridos;
	 Map<TipoRol, List<ArtistaService>> asignaciones;
	 
	 public CancionRepository(String nombre, TipoEstado estado, int duracion,
			Map<TipoRol, List<ArtistaService>> asignaciones) {
		super();
		this.nombre = nombre;
		this.estado = estado;
		this.duracion = duracion;
		this.asignaciones = asignaciones;
	 }
	 
	 public String getNombre() {
		 return nombre;
	 }
	 
	 public TipoEstado getEstado() {
		 return estado;
	 }
	 
	 public int getDuracion() {
		 return duracion;
	 }
	 
	 public Map<TipoRol, List<ArtistaService>> getAsignaciones() {
		 return asignaciones;
	 }
	 
}
