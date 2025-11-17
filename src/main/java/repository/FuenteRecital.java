package repository;

public interface FuenteRecital {
	RecitalRepository cargar();
	void guardar(RecitalRepository recital);
}
