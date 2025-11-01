package repository;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFuenteRecital implements FuenteRecital{
	protected String path;
	
	public JsonFuenteRecital(String path) {
		this.path = path;
	}


	@Override
	public List<RecitalRepository> cargar() {
		try {
            File jsonFile = new File(this.path);
            
            final ObjectMapper mapper = new ObjectMapper();
            
            RecitalRepository repositoryCargado = mapper.readValue(jsonFile, RecitalRepository.class);
            
            RecitalRepository.setInstancia(repositoryCargado);

            return Collections.singletonList(RecitalRepository.getInstancia(repositoryCargado.getTitulo()));
            
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
        RecitalRepository repositorioAGuardar = recitales.get(0);
        
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            File recital = new File(path);
            
            mapper.writeValue(recital, repositorioAGuardar);
            
            System.out.println("Estado del RecitalRepository guardado con éxito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar el Repositorio en JSON: " + path);
            e.printStackTrace();
        }
		
	}
}
