package repository;

import domain.Cancion;
import java.util.List;

public class JsonFuenteCancion implements FuenteCancion {
    private String path;
    private DataLoader loader;
    
    public JsonFuenteCancion(String path) {
        this.path = path;
        this.loader = new DataLoader();
    }
    
    @Override
    public List<Cancion> cargar() {
        return loader.cargarCanciones(path);
    }
    
    @Override
    public void guardar(List<Cancion> canciones) {
        // TODO: Implementar guardado (Bonus)
        throw new UnsupportedOperationException("Guardado no implementado aún");
    }
}