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
    private DataLoader loader;
    
    public JsonFuenteArtista(Path path) {
    	this.path = Objects.requireNonNull(path, "path");
        this.loader = new DataLoader();
    }
    @Override
    public List<Artista> cargar() {
        try {
            File jsonFile = this.path.toFile();
            
            // ObjectMapper es el encargado de la conversión JSON <-> Java
            final ObjectMapper mapper = new ObjectMapper();
            
            // TypeReference es necesario para deserializar colecciones (List<Artista>)
            List<Artista> artistasCargados = mapper.readValue(jsonFile, new TypeReference<List<Artista>>() {});
            
            // Cargar los artistas en el ArtistaRepository (Singleton)
            ArtistaRepository repositorio = ArtistaRepository.getInstance();
            repositorio.limpiar(); // Limpiar el repositorio antes de cargar nuevos datos
            artistasCargados.forEach(repositorio::agregar);
            
            System.out.println("Artistas cargados con éxito desde: " + path);

            return artistasCargados; 

        } catch (IOException e) {
            System.err.println("Error al cargar artistas desde JSON: " + e.getMessage());
            e.printStackTrace();
            // Devolver la lista actual del repositorio, o vacía si falló
            return ArtistaRepository.getInstance().obtenerTodos(); 
        }
    }

    // --- GUARDAR ---
    @Override
    public void guardar(List<Artista> artistas) {
        if (artistas == null || artistas.isEmpty()) {
             System.err.println("Advertencia: No hay artistas para guardar.");
             return;
        }
        
        // Habilita INDENT_OUTPUT para que el JSON guardado sea legible (pretty print)
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        
        try {
            File artistaFile = this.path.toFile();
            
            // Escribir la lista completa de Artistas
            // Jackson usará el Artista.getRolesParaGuardar() para serializar el formato Array
            mapper.writeValue(artistaFile, artistas);
            
            System.out.println("Lista de Artistas guardada con Éxito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar Artistas en JSON: " + path);
            e.printStackTrace();
        }
    }
}