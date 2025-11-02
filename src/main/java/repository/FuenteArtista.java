package repository;

import domain.Artista;
import java.util.List;

public interface FuenteArtista {
    List<Artista> cargar();
    void guardar(List<Artista> artistas);
}