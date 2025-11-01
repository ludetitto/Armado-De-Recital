package repository;

import java.util.List;
import java.util.Set;

import services.CancionService;
import services.ContratacionService;

public class RecitalRepository {
	String titulo;
    Set<CancionService> canciones;
    List<ContratacionService> contratos;
    RecitalRepository instance;
    
    RecitalRepository getInstance() {
    	return this;
    }
}
