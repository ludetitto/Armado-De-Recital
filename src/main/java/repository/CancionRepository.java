package repository;

import domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class CancionRepository {
    private Map<String, Cancion> canciones; // Mapa por título
    
    public CancionRepository() {
        this.canciones = new LinkedHashMap<>();
    }
    
    // Agregar canción
    public void agregar(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser null");
        }
        canciones.put(cancion.getTitulo(), cancion);
    }
    
    // Buscar canción por título
    public Cancion buscarPorTitulo(String titulo) {
        return canciones.get(titulo);
    }
    
    // Verificar si existe una canción
    public boolean existe(String titulo) {
        return canciones.containsKey(titulo);
    }
    
    // Obtener todas las canciones
    public List<Cancion> obtenerTodas() {
        return new ArrayList<>(canciones.values());
    }
    
    // Obtener canciones por estado
    public List<Cancion> obtenerPorEstado(TipoEstado estado) {
        return canciones.values().stream()
            .filter(c -> c.getEstado() == estado)
            .collect(Collectors.toList());
    }
    
    // Obtener canciones completas
    public List<Cancion> obtenerCompletas() {
        return obtenerPorEstado(TipoEstado.COMPLETA);
    }
    
    // Obtener canciones incompletas
    public List<Cancion> obtenerIncompletas() {
        return canciones.values().stream()
            .filter(c -> c.getEstado() != TipoEstado.COMPLETA)
            .collect(Collectors.toList());
    }
    
    // Obtener canciones que requieren un rol específico
    public List<Cancion> obtenerPorRolRequerido(TipoRol rol) {
        return canciones.values().stream()
            .filter(c -> c.getRolesRequeridos().containsKey(rol))
            .collect(Collectors.toList());
    }
    
    // Eliminar canción
    public void eliminar(String titulo) {
        canciones.remove(titulo);
    }
    
    // Limpiar todas las canciones
    public void limpiar() {
        canciones.clear();
    }
    
    // Obtener cantidad de canciones
    public int cantidad() {
        return canciones.size();
    }
}