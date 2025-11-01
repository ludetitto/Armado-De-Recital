package repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RecitalRepository {
	String titulo;
    Set<CancionRepository> canciones;
    List<ContratacionRepository> contratos;
    private static RecitalRepository instance;
    
    private RecitalRepository(String titulo) {
        this.titulo = titulo;
//        this.canciones = new HashSet<>(); 
//        this.contratos = new ArrayList<>();
    }

    // para el json
    @JsonCreator
    private RecitalRepository(@JsonProperty("titulo") String titulo,
                              @JsonProperty("canciones") Set<CancionRepository> canciones,
                              @JsonProperty("contratos") List<ContratacionRepository> contratos) {
        
        this.titulo = titulo;
        // inicialización de colecciones para evitar NullPointerException
        this.canciones = canciones != null ? canciones : new HashSet<>(); 
        this.contratos = contratos != null ? contratos : new ArrayList<>();
    }
   
    public static synchronized RecitalRepository getInstancia(String tituloRecital) {
        if (instance == null) {
            // si la instancia no existe, la creamos con el tituloRecital

            instance = new RecitalRepository(tituloRecital);
        }
        return instance;
    }
    
 // este método forzado permite a la clase de carga JSON establecer la instancia Singleton.
    public static void setInstancia(RecitalRepository repository) {
        RecitalRepository.instance = repository;
    }

	public Set<CancionRepository> getCanciones() {
		return canciones;
	}

	public void setCanciones(Set<CancionRepository> canciones) {
		this.canciones = canciones;
	}

	public List<ContratacionRepository> getContratos() {
		return contratos;
	}

	public void setContratos(List<ContratacionRepository> contratos) {
		this.contratos = contratos;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	

    
    
}
