package domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Cancion {
    private String titulo;
    private TipoEstado estado;
    private Map<TipoRol, Integer> rolesRequeridos;
    
    private Map<TipoRol, List<Artista>> asignaciones;
    
    public Cancion(String titulo, Map<TipoRol, Integer> rolesRequeridos) {
        this.titulo = titulo;
        this.rolesRequeridos = new EnumMap<>(rolesRequeridos);
        this.asignaciones = new EnumMap<>(TipoRol.class);
        this.estado = TipoEstado.BORRADOR;
        actualizarEstado();
    }
    
    protected Cancion() {
        // Inicialización mínima si es necesario, o dejar vacío.
        this.rolesRequeridos = new HashMap<>();
        this.asignaciones = new HashMap<>();
    }
    
    // Asignar artista a un rol
    public void asignarArtista(Artista artista, TipoRol rol) {
        if (!artista.puedeOcuparRol(rol)) {
            throw new IllegalArgumentException(
                artista.getNombre() + " no puede ocupar el rol: " + rol
            );
        }
        
        asignaciones.computeIfAbsent(rol, k -> new ArrayList<>()).add(artista);
        actualizarEstado();
    }
    
    // Desasignar artista de un rol
    public void desasignarArtista(Artista artista, TipoRol rol) {
        List<Artista> artistasEnRol = asignaciones.get(rol);
        if (artistasEnRol != null) {
            artistasEnRol.remove(artista);
            if (artistasEnRol.isEmpty()) {
                asignaciones.remove(rol);
            }
        }
        actualizarEstado();
    }
    
    // Calcular roles faltantes
    public Map<TipoRol, Integer> getRolesFaltantes() {
        Map<TipoRol, Integer> faltantes = new EnumMap<>(TipoRol.class);
        
        for (Map.Entry<TipoRol, Integer> entry : rolesRequeridos.entrySet()) {
            TipoRol rol = entry.getKey();
            int requeridos = entry.getValue();
            int asignados = asignaciones.getOrDefault(rol, Collections.emptyList()).size();
            int faltante = requeridos - asignados;
            
            if (faltante > 0) {
                faltantes.put(rol, faltante);
            }
        }
        
        return faltantes;
    }
    
    // Verificar si la canción está completa
    public boolean estaCompleta() {
        return getRolesFaltantes().isEmpty();
    }
    
    // Actualizar estado de la canción
    private void actualizarEstado() {
        if (asignaciones.isEmpty()) {
            estado = TipoEstado.BORRADOR;
        } else if (estaCompleta()) {
            estado = TipoEstado.COMPLETA;
        } else {
            estado = TipoEstado.INCOMPLETA;
        }
    }
    
    // Getters
    
    
    public String getTitulo() { return titulo; }
    public TipoEstado getEstado() { return estado; }


    public Map<TipoRol, Integer> getRolesRequeridos() { 
        return Collections.unmodifiableMap(rolesRequeridos); 
    }

    public Map<TipoRol, List<Artista>> getAsignaciones() { 
        return Collections.unmodifiableMap(asignaciones); 
    }
    
 // lo usa Jackson para inyectar el mapa cargado.
    public void setAsignaciones(Map<TipoRol, List<Artista>> asignacionesCargadas) {
        this.asignaciones = asignacionesCargadas;
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - Roles requeridos: %s", 
            titulo, estado, rolesRequeridos);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cancion cancion = (Cancion) o;
        return titulo.equals(cancion.titulo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }
}