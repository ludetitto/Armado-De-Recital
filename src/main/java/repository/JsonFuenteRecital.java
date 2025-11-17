package repository;

import domain.Artista;
import domain.Cancion;
import domain.Recital; 
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFuenteRecital implements FuenteRecital {

    private final Path path;
    private ArtistaRepository repositoryArtistas; 
    private CancionRepository repositoryCanciones;

    
    public JsonFuenteRecital(Path path) {
		this.path = Objects.requireNonNull(path, "path");
	}
    
    public JsonFuenteRecital(Path path, ArtistaRepository repositoryArtistas, CancionRepository repositoryCanciones) {
		this.path = Objects.requireNonNull(path, "path");
		this.repositoryArtistas = repositoryArtistas;
		this.repositoryCanciones = repositoryCanciones;
	}

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "JsonFuenteRecital{path=" + path + "}";
    }

    @Override
    public RecitalRepository cargar() {
        try {
        	
        	List<Artista> todosLosArtistas;
        	Set<Cancion> canciones;
        	
            File jsonFile = this.path.toFile();
            
            final ObjectMapper mapper = new ObjectMapper();
            
            Recital recitalCargado = mapper.readValue(jsonFile, Recital.class);
            
            Recital.setInstance(recitalCargado); 
            
            todosLosArtistas = recitalCargado.getArtistasTodos();
            
            for(Artista a : todosLosArtistas) {
            	repositoryArtistas.agregar(a);
            }
            
            canciones= recitalCargado.getCanciones();
            
            for (Cancion  c: canciones) {
            	repositoryCanciones.agregar(c);
            }
            
            return new RecitalRepository(recitalCargado);
            
        } catch (IOException e) {
            System.err.println("Error al cargar el JSON en el Repositorio: " + e.getMessage());
        }
		return null;
    }


	@Override
    public void guardar(RecitalRepository recital) {
        if (recital == null) {
            System.err.println("Advertencia: No hay instancia de Repositorio para guardar.");
            return;
        }
        
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        
        try {
            File recitalFile = this.path.toFile();
          
            mapper.writeValue(recitalFile, recital);
            
            System.out.println("Estado del Recital guardado con Exito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar el Recital en JSON: " + path);
            e.printStackTrace();
        }
    }
}
