package repository;

import domain.Artista;
import domain.Recital;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFuenteArtista implements FuenteArtista {

	private final Logger logger = Logger.getLogger(JsonFuenteArtista.class.getName());
	
    private final Path path;
    private final ArtistaRepository repository; 
    private final ObjectMapper mapper;
    
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
            
            List<Artista> artistasCargados = this.mapper.readValue(jsonFile, new TypeReference<List<Artista>>() {});
            
            this.repository.limpiar(); 
            artistasCargados.forEach(this.repository::agregar);
            
            Recital recitalInstanciado= Recital.getInstance();
            
            recitalInstanciado.agregarArtistas(artistasCargados);

            return artistasCargados; 

        } catch (IOException e) {
        	logger.severe("Error al cargar artistas desde JSON: " + e.getMessage());
            e.printStackTrace();
            return this.repository.obtenerTodos(); 
        }
    }

    @Override
    public void guardar(List<Artista> artistas) {
        if (artistas == null || artistas.isEmpty()) {
        	logger.warning("No hay artistas para guardar.");
             return;
        }
        
        try {
            File artistaFile = this.path.toFile();
            
            this.mapper.writeValue(artistaFile, artistas);
            
            logger.info("Lista de Artistas guardada con exito en: " + path);

        } catch (IOException e) {
        	logger.severe("ERROR al guardar Artistas en JSON: " + path);
            e.printStackTrace();
        }
    }
}