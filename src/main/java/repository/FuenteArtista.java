package repository;

import java.util.List;

public interface FuenteArtista {
	List<ArtistaRepository> cargar();
	void guardar(List<ArtistaRepository> recitales);
}
