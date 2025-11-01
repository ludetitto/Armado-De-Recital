package repository;

import domain.Cancion;
import java.util.List;

public interface FuenteCancion {
    List<Cancion> cargar();
    void guardar(List<Cancion> canciones);
}