package repository;

import domain.Artista;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFuenteArtista implements FuenteArtista {

    private final Path path;
    //  El repositorio ya no es Singleton, debe ser pasado o creado.
    private final ArtistaRepository repository; 
    private final ObjectMapper mapper;
    
    // Constructor que recibe la ruta y el repositorio al que debe cargar/guardar
    public JsonFuenteArtista(Path path, ArtistaRepository repository) {
    	this.path = Objects.requireNonNull(path, "path");
        this.repository = Objects.requireNonNull(repository, "repository");
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public List<Artista> cargar() {
        try {
            File jsonFile = this.path.toFile();
            
            // Deserializar List<Artista>
            List<Artista> artistasCargados = this.mapper.readValue(jsonFile, new TypeReference<List<Artista>>() {});
            
            // Cargar en la instancia de Repositorio que se pasó al constructor
            this.repository.limpiar(); 
            artistasCargados.forEach(this.repository::agregar);
            
            System.out.println("Artistas cargados con éxito desde: " + path);

            return artistasCargados; 

        } catch (IOException e) {
            System.err.println("Error al cargar artistas desde JSON: " + e.getMessage());
            e.printStackTrace();
            // Devolver la lista actual de la instancia del repositorio
            return this.repository.obtenerTodos(); 
        }
    }

    @Override
    public void guardar(List<Artista> artistas) {
        if (artistas == null || artistas.isEmpty()) {
             System.err.println("Advertencia: No hay artistas para guardar.");
             return;
        }
        
        try {
            File artistaFile = this.path.toFile();
            
            // Serializar List<Artista>
            this.mapper.writeValue(artistaFile, artistas);
            
            System.out.println("Lista de Artistas guardada con exito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar Artistas en JSON: " + path);
            e.printStackTrace();
        }
    }
}