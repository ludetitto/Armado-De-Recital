package repository;

import domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class ArtistaRepository {
    private Map<String, Artista> artistas; // Mapa por nombre para búsqueda rápida
    
    public ArtistaRepository() {
        this.artistas = new LinkedHashMap<>();
    }
    
    // Agregar artista
    public void agregar(Artista artista) {
        if (artista == null) {
            throw new IllegalArgumentException("El artista no puede ser null");
        }
        artistas.put(artista.getNombre(), artista);
    }
    
    // Buscar artista por nombre
    public Artista buscarPorNombre(String nombre) {
        return artistas.get(nombre);
    }
    
    // Verificar si existe un artista
    public boolean existe(String nombre) {
        return artistas.containsKey(nombre);
    }
    
    // Obtener todos los artistas
    public List<Artista> obtenerTodos() {
        return new ArrayList<>(artistas.values());
    }
    
    // Obtener solo artistas base
    public List<Artista> obtenerArtistasBase() {
        return artistas.values().stream()
            .filter(Artista::esBase)
            .collect(Collectors.toList());
    }
    
    // Obtener solo artistas externos
    public List<Artista> obtenerArtistasExternos() {
        return artistas.values().stream()
            .filter(a -> !a.esBase())
            .map(a -> a)
            .collect(Collectors.toList());
    }
    
    // Buscar artistas que pueden ocupar un rol específico
    public List<Artista> buscarPorRol(TipoRol rol) {
        return artistas.values().stream()
            .filter(a -> a.puedeOcuparRol(rol))
            .collect(Collectors.toList());
    }
    
    // Buscar artistas que pertenecieron a una banda
    public List<Artista> buscarPorBanda(String banda) {
        return artistas.values().stream()
            .filter(a -> a.getBandas().contains(banda))
            .collect(Collectors.toList());
    }
    
    // Eliminar artista
    public void eliminar(String nombre) {
        artistas.remove(nombre);
    }
    
    // Limpiar todos los artistas
    public void limpiar() {
        artistas.clear();
    }
    
    // Obtener cantidad de artistas
    public int cantidad() {
        return artistas.size();
    }
}