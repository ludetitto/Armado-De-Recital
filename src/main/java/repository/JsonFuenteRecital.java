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
            // 1. Leer el objeto RecitalRepository desde el archivo JSON
            File jsonFile = new File(this.path);
            
            final ObjectMapper mapper = new ObjectMapper();
			// Jackson lee el JSON y crea un nuevo objeto RecitalRepository
            RecitalRepository repositoryCargado = mapper.readValue(jsonFile, RecitalRepository.class);
            
            // 2. Establecer el Singleton: Usamos el método estático para reemplazar
            //    o establecer la instancia única con el objeto deserializado.
            //    Esto es clave para usar el JSON como fuente de inicialización.
            RecitalRepository.setInstancia(repositoryCargado);

            // 3. Devolver el Singleton como una lista de un solo elemento (por la firma)
            return Collections.singletonList(RecitalRepository.getInstancia(repositoryCargado.getTitulo()));
            
        } catch (IOException e) {
            // Manejo de errores de lectura/mapeo (archivo no encontrado, JSON inválido, etc.)
            System.err.println("Error al cargar el JSON en el Repositorio: " + e.getMessage());
            e.printStackTrace();
            
            // En caso de error, devolvemos el Singleton vacío/default (si ya existe) 
            // o una lista vacía.
            return Collections.emptyList();
        }

    }


	@Override
	public void guardar(List<RecitalRepository> recitales) {
		// Asumimos que la lista contiene una sola instancia (el Singleton)
        if (recitales == null || recitales.isEmpty()) {
            System.err.println("Advertencia: No hay instancia de Repositorio para guardar.");
            return;
        }
        RecitalRepository repositorioAGuardar = recitales.get(0);
        
        // Objeto central de la serialización
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            // 1. Especifica el archivo de destino
            File recital = new File(path);
            
            /* 2. Ejecuta la serialización: convierte el objeto repositorioAGuardar a JSON 
            	y lo escribe en el archivo archivoDeSalida*/
            mapper.writeValue(recital, repositorioAGuardar);
            
// No me gusta que este el System.out.println adentro de esta clase, se podrias poner que devuelve un booleano
            System.out.println("Estado del RecitalRepository guardado con éxito en: " + path);

        } catch (IOException e) {
            System.err.println("ERROR al guardar el Repositorio en JSON: " + path);
            e.printStackTrace();
        }
		
	}
}
