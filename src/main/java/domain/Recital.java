package domain;

import java.util.*;
import java.util.stream.Collectors;

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
   
    public void agregarCancion(Cancion cancion) {
        canciones.add(cancion);
    }
    
    public void modificarAsiganacion(Artista artista, TipoRol rol, Cancion cancion) {
    	
    }
    
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
    
    public void agregarArtistas( List<Artista> artistas) {
        if (artistas == null || artistas.isEmpty()) {
            return;
        }

        for (Artista artista : artistas) {
            if (artista == null || (this.artistasBase.contains(artista) || this.artistasCandidatos.contains(artista)  )) {
                continue; 
            }
            
            if (artista.esBase()) {
                this.artistasBase.add(artista); 
            } else {
                this.artistasCandidatos.add(artista);
            }
        }
    }
    
    public void cargarCanciones(List<Cancion>canciones) {
        if (canciones == null) {
            return;
        }

        for (Cancion cancion : canciones) {
            if (cancion != null && canciones.contains(cancion)) {
                this.canciones.add(cancion);
            }
        }
    }
    
    public void designarArtistadeCancion(Cancion cancion) {
    	this.canciones.remove(cancion);
    	this.canciones.add(cancion);
    }
    
    public void agregarContratacion(Contratacion contratacion) {
        contrataciones.add(contratacion);
    }
    
    public void eliminarContratacion(Contratacion contratacion) {
        contrataciones.remove(contratacion);
        artistasCandidatos.add(contratacion.getArtista());
    }
    
    public double calcularCostoTotal() {
        return contrataciones.stream()
            .mapToDouble(Contratacion::getCostoFinal)
            .sum();
    }
    
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
    
    public boolean estaContratadoEnCancion(Artista artista, Cancion cancion) {
        return cancion.getArtistasAsignados().contains(artista);
    }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public Set<Cancion> getCanciones() { return Collections.unmodifiableSet(canciones); }
    public List<Contratacion> getContrataciones() { return Collections.unmodifiableList(contrataciones); }


   
    public List<Artista> getArtistasBase() { return Collections.unmodifiableList(artistasBase); }
   
    public List<Artista> getArtistasCandidatos() { return Collections.unmodifiableList(artistasCandidatos); }

    @JsonIgnore
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

    public static void setInstance(Recital recitalCargado) {
        if (recitalCargado == null) {
            throw new IllegalArgumentException("La instancia de Recital cargada no puede ser nula.");
        }
        Recital.instance = recitalCargado;
    }
    
    @JsonIgnore
    public boolean estaContratado(Artista artista) {
		List<Contratacion> contrataciones = getContrataciones();
		boolean estaContratado = false;
		
		for(Contratacion c : contrataciones) {
			if(c.getArtista().equals(artista))
				estaContratado = true;
		}
		
		return estaContratado;
	}
    
    @JsonIgnore
    public Artista obtenerArtistaPorNombre(String nombreArtista) {
		return  getArtistas().stream()
		        .filter(c -> c.getNombre().equalsIgnoreCase(nombreArtista))
		        .findFirst().orElse(null);
    }
    
    @JsonIgnore
    public Cancion obtenerCancionPorNombre(String tituloCancion) {
		return  getCanciones().stream()
		        .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
		        .findFirst().orElse(null);
    }

    @JsonIgnore
    public Contratacion obtenerContratacionPorArtistaYCancion(Cancion cancion, Artista artista) {
		return  getContrataciones().stream()
		        .filter(c -> 
	            c.getArtista().equals(artista) &&
	            c.getCancion().equals(cancion)
		        )
		        .findFirst()
		        .orElse(null);
    }

	public List<Artista> getArtistasConContratacion() {

		return this.contrataciones.stream()
	            .map(c -> c.getArtista())
	            .distinct()
	            .collect(Collectors.toList());
		
	}
    
}