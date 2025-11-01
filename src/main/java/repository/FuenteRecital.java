package repository;

import java.util.List;

public interface FuenteRecital {
	List<RecitalRepository> cargar();
	void guardar(List<RecitalRepository> recitales);
}
