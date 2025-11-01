package repository;

import domain.Cancion;
import java.util.List;

public class XmlFuenteCancion implements FuenteCancion {
    private String path;
    
    public XmlFuenteCancion(String path) {
        this.path = path;
    }
    
    @Override
    public List<Cancion> cargar() {
        // TODO: Implementar carga XML
        throw new UnsupportedOperationException("Carga XML no implementada aún");
    }
    
    @Override
    public void guardar(List<Cancion> canciones) {
        // TODO: Implementar guardado XML
        throw new UnsupportedOperationException("Guardado XML no implementado aún");
    }
}