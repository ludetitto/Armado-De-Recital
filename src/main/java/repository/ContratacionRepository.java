package repository;

import domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class ContratacionRepository {
    private List<Contratacion> contrataciones;
    
    public ContratacionRepository() {
        this.contrataciones = new ArrayList<>();
    }
    
    public void agregar(Contratacion contratacion) {
        if (contratacion == null) {
            throw new IllegalArgumentException("La contratación no puede ser null");
        }
        contrataciones.add(contratacion);
    }
        public List<Contratacion> obtenerTodas() {
        return new ArrayList<>(contrataciones);
    }
    
    public List<Contratacion> obtenerPorArtista(Artista artista) {
        return contrataciones.stream()
            .filter(c -> c.getArtista().equals(artista))
            .collect(Collectors.toList());
    }
    
    public List<Contratacion> obtenerPorCancion(Cancion cancion) {
        return contrataciones.stream()
            .filter(c -> c.getCancion().equals(cancion))
            .collect(Collectors.toList());
    }
    
    public List<Contratacion> obtenerPorRol(TipoRol rol) {
        return contrataciones.stream()
            .filter(c -> c.getRol() == rol)
            .collect(Collectors.toList());
    }
    
    public Contratacion buscar(Artista artista, Cancion cancion, TipoRol rol) {
        return contrataciones.stream()
            .filter(c -> c.getArtista().equals(artista) && 
                         c.getCancion().equals(cancion) && 
                         c.getRol() == rol)
            .findFirst()
            .orElse(null);
    }
    
    public boolean existe(Artista artista, Cancion cancion, TipoRol rol) {
        return buscar(artista, cancion, rol) != null;
    }
    
    public boolean eliminar(Contratacion contratacion) {
        return contrataciones.remove(contratacion);
    }
    
    public void eliminarPorArtista(Artista artista) {
        contrataciones.removeIf(c -> c.getArtista().equals(artista));
    }
    
    public void eliminarPorCancion(Cancion cancion) {
        contrataciones.removeIf(c -> c.getCancion().equals(cancion));
    }
    
    public double calcularCostoTotal() {
        return contrataciones.stream()
            .mapToDouble(Contratacion::getCostoFinal)
            .sum();
    }
    
    public void limpiar() {
        contrataciones.clear();
    }
    
    public int cantidad() {
        return contrataciones.size();
    }
}