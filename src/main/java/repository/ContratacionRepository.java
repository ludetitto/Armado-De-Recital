package repository;

import domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class ContratacionRepository {
    private List<Contratacion> contrataciones;
    
    public ContratacionRepository() {
        this.contrataciones = new ArrayList<>();
    }
    
    // Agregar contratación
    public void agregar(Contratacion contratacion) {
        if (contratacion == null) {
            throw new IllegalArgumentException("La contratación no puede ser null");
        }
        contrataciones.add(contratacion);
    }
    
    // Obtener todas las contrataciones
    public List<Contratacion> obtenerTodas() {
        return new ArrayList<>(contrataciones);
    }
    
    // Obtener contrataciones de un artista específico
    public List<Contratacion> obtenerPorArtista(Artista artista) {
        return contrataciones.stream()
            .filter(c -> c.getArtista().equals(artista))
            .collect(Collectors.toList());
    }
    
    // Obtener contrataciones de una canción específica
    public List<Contratacion> obtenerPorCancion(Cancion cancion) {
        return contrataciones.stream()
            .filter(c -> c.getCancion().equals(cancion))
            .collect(Collectors.toList());
    }
    
    // Obtener contrataciones por rol
    public List<Contratacion> obtenerPorRol(TipoRol rol) {
        return contrataciones.stream()
            .filter(c -> c.getRol() == rol)
            .collect(Collectors.toList());
    }
    
    // Buscar contratación específica
    public Contratacion buscar(Artista artista, Cancion cancion, TipoRol rol) {
        return contrataciones.stream()
            .filter(c -> c.getArtista().equals(artista) && 
                         c.getCancion().equals(cancion) && 
                         c.getRol() == rol)
            .findFirst()
            .orElse(null);
    }
    
    // Verificar si existe una contratación
    public boolean existe(Artista artista, Cancion cancion, TipoRol rol) {
        return buscar(artista, cancion, rol) != null;
    }
    
    // Eliminar contratación específica
    public boolean eliminar(Contratacion contratacion) {
        return contrataciones.remove(contratacion);
    }
    
    // Eliminar todas las contrataciones de un artista
    public void eliminarPorArtista(Artista artista) {
        contrataciones.removeIf(c -> c.getArtista().equals(artista));
    }
    
    // Eliminar todas las contrataciones de una canción
    public void eliminarPorCancion(Cancion cancion) {
        contrataciones.removeIf(c -> c.getCancion().equals(cancion));
    }
    
    // Calcular costo total de todas las contrataciones
    public double calcularCostoTotal() {
        return contrataciones.stream()
            .mapToDouble(Contratacion::getCostoFinal)
            .sum();
    }
    
    // Limpiar todas las contrataciones
    public void limpiar() {
        contrataciones.clear();
    }
    
    // Obtener cantidad de contrataciones
    public int cantidad() {
        return contrataciones.size();
    }
}