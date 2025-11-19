package repository;

import domain.Cancion;

import java.util.*;

public class CancionRepository {

	// UTILS
    private final Map<String, Cancion> porTitulo = new LinkedHashMap<>();

    public void agregar(Cancion cancion) {
        porTitulo.put(Objects.requireNonNull(cancion).getTitulo(), cancion);
    }

    public Cancion buscarPorTitulo(String titulo) {
        return porTitulo.get(titulo);
    }

    public int cantidad() {
        return porTitulo.size();
    }

    public Collection<Cancion> todas() {
        return Collections.unmodifiableCollection(porTitulo.values());
    }

    public void limpiar() {
        porTitulo.clear();
    }

    public List<Cancion> obtenerTodas() {
        return new ArrayList<>(porTitulo.values());
    }
}
