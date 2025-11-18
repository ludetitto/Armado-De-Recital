package domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Recital {
    private static Recital instance;
    
    private String titulo;
    
    private Set<Cancion> canciones;
    
    private List<Contratacion> contrataciones;
    
    private List<Artista> artistasBase;
    
    private List<Artista> artistasCandidatos;
    
    private Recital() {
        this.canciones = new LinkedHashSet<>();
        this.contrataciones = new ArrayList<>();
        this.artistasBase = new ArrayList<>();
        this.artistasCandidatos = new ArrayList<>();
        this.titulo = "Recital Especial";
    }
    
    public static Recital getInstance() {
        if (instance == null) {
            instance = new Recital();
        }
        return instance;
    }
   
    
    // Agregar canción
    public void agregarCancion(Cancion cancion) {
        canciones.add(cancion);
    }
    
    public void modificarAsiganacion(Artista artista, TipoRol rol, Cancion cancion) {
    	
    }
    
    
    // Agregar artista base
    public void agregarArtistaBase(Artista artista) {
        if (artista.esBase()) {
            artistasBase.add(artista);
        }
    }
    
    public void agregarArtistaCandidato(Artista artista) {
        if (artista.esExterno()) { 
            artistasCandidatos.add(artista);
        }
    }
    
    // agrgar al recital los artistas cargados desde el repositorio
    public void agregarArtistas( List<Artista> artistas) {
        if (artistas == null || artistas.isEmpty()) {
            return;
        }

        for (Artista artista : artistas) {
            if (artista == null && (this.artistasBase.contains(artista) || this.artistasCandidatos.contains(artista)  )) {
                continue; 
            }
            
            if (artista.esBase() ) { // o que este contratado

                this.artistasBase.add(artista); 
            } else {
                this.artistasCandidatos.add(artista);
            }
        }
    }
    
    // cancion repositorio
    public void cargarCanciones(List<Cancion>canciones) {
        if (canciones == null) {
            return;
        }

        for (Cancion cancion : canciones) {
            if (cancion != null) {
                this.canciones.add(cancion);
            }
        }
    }
    
    //eliminar artista
    public void designarArtistadeCancion(Cancion cancion) {
    	this.canciones.remove(cancion);
    	this.canciones.add(cancion);
    }
    
    // Agregar contratación
    public void agregarContratacion(Contratacion contratacion) {
        contrataciones.add(contratacion);
        artistasCandidatos.remove(contratacion.getArtista());
    }
    
    // Eliminar contratación
    public void eliminarContratacion(Contratacion contratacion) {
        contrataciones.remove(contratacion);
        artistasCandidatos.add(contratacion.getArtista());
    }
    
    // Calcular costo total
    public double calcularCostoTotal() {
        return contrataciones.stream()
            .mapToDouble(Contratacion::getCostoFinal)
            .sum();
    }
    
    // Obtener roles faltantes para todo el recital
    public Map<TipoRol, Integer> getRolesFaltantesTotal() {
        Map<TipoRol, Integer> faltantesTotal = new EnumMap<>(TipoRol.class);
        
        for (Cancion cancion : canciones) {
            Map<TipoRol, Integer> faltantesCancion = cancion.getRolesFaltantes();
            faltantesCancion.forEach((rol, cantidad) -> 
                faltantesTotal.merge(rol, cantidad, Integer::sum)
            );
        }
        
        return faltantesTotal;
    }
    
    // Getters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public Set<Cancion> getCanciones() { return Collections.unmodifiableSet(canciones); }
    public List<Contratacion> getContrataciones() { return Collections.unmodifiableList(contrataciones); }
    
    @JsonIgnore
    public List<Artista> getArtistasBase() { return Collections.unmodifiableList(artistasBase); }
    @JsonIgnore
    public List<Artista> getArtistasCandidatos() { return Collections.unmodifiableList(artistasCandidatos); }

    public List<Artista> getArtistas(){
    	List<Artista> todosLosArtistas = new ArrayList<>();

        if (artistasBase != null) {
            todosLosArtistas.addAll(artistasBase);
        }
        if (artistasCandidatos != null) {
            todosLosArtistas.addAll(artistasCandidatos);
        }
        
        return todosLosArtistas;
    }
    
  
    @Override
    public String toString() {
        return String.format("Recital: %s - Canciones: %d - Contrataciones: %d - Costo Total: $%.2f",
            titulo, canciones.size(), contrataciones.size(), calcularCostoTotal());
    }

    // set necesario para cargar de un json
    public static void setInstance(Recital recitalCargado) {
        if (recitalCargado == null) {
            throw new IllegalArgumentException("La instancia de Recital cargada no puede ser nula.");
        }
        Recital.instance = recitalCargado;
    }

}