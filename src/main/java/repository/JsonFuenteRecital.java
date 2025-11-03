package repository;

import domain.Recital; // Ahora serializamos/deserializamos domain.Recital
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFuenteRecital implements FuenteRecital {

    private final Path path;

    public JsonFuenteRecital(Path path) {
        this.path = Objects.requireNonNull(path, "path");
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "JsonFuenteRecital{path=" + path + "}";
    }

    @Override
    public List<RecitalRepository> cargar() {
        try {
            File jsonFile = this.path.toFile();
            
            final ObjectMapper mapper = new ObjectMapper();
            
            Recital recitalCargado = mapper.readValue(jsonFile, Recital.class);
            
            Recital.setInstance(recitalCargado); 

            RecitalRepository.setRecitalInstance(recitalCargado);
            
            return Collections.singletonList(RecitalRepository.getInstance());
            
        } catch (IOException e) {
            System.err.println("Error al cargar el JSON en el Repositorio: " + e.getMessage());
            e.printStackTrace();
            
            return Collections.emptyList();
        }
    }


    @Override
    public void guardar(List<RecitalRepository> recitales) {
        if (recitales == null || recitales.isEmpty()) {
            System.err.println("Advertencia: No hay instancia de Repositorio para guardar.");
            return;
        }
        
        Recital recitalAGuardar = recitales.get(0).getRecital(); 
        
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        
        try {
            File recitalFile = this.path.toFile();
          
            mapper.writeValue(recitalFile, recitalAGuardar);
            
            System.out.println("Estado del Recital guardado con éxito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar el Recital en JSON: " + path);
            e.printStackTrace();
        }
    }
}
