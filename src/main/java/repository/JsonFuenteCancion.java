package repository;

import domain.Cancion;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFuenteCancion implements FuenteCancion {

    private final Path path;
    private final CancionRepository repository; 
    private final ObjectMapper mapper;
    
    public JsonFuenteCancion(Path path, CancionRepository repository) {
    	this.path = Objects.requireNonNull(path, "path");
        this.repository = Objects.requireNonNull(repository, "repository");
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // --- CARGAR ---
    @Override
    public List<Cancion> cargar() {
        try {
            File jsonFile = this.path.toFile();
            
            List<Cancion> cancionesCargadas = this.mapper.readValue(jsonFile, new TypeReference<List<Cancion>>() {});
            
            // Cargar en la instancia de Repositorio
            this.repository.limpiar(); 
            cancionesCargadas.forEach(this.repository::agregar);
            
            System.out.println("Canciones cargadas con Exito desde: " + path);

            return cancionesCargadas; 

        } catch (IOException e) {
            System.err.println("Error al cargar canciones desde JSON: " + e.getMessage());
            e.printStackTrace();
            return this.repository.obtenerTodas(); 
        }
    }

    // --- GUARDAR ---
    @Override
    public void guardar(List<Cancion> canciones) {
         if (canciones == null || canciones.isEmpty()) {
             System.err.println("Advertencia: No hay canciones para guardar.");
             return;
        }
        
        try {
            File cancionFile = this.path.toFile();
            
            // Serializar List<Cancion>
            this.mapper.writeValue(cancionFile, canciones);
            
            System.out.println("Lista de Canciones guardada con Exito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar Canciones en JSON: " + path);
            e.printStackTrace();
        }
    }
}