package repository;

import domain.Artista;

import java.util.*;

public class ArtistaRepository {

	// UTILS
    private final Map<String, Artista> porNombre = new LinkedHashMap<>();

    public void agregar(Artista artista) {
        porNombre.put(Objects.requireNonNull(artista).getNombre(), artista);
    }

    public Artista buscarPorNombre(String nombre) {
        return porNombre.get(nombre);
    }

    public int cantidad() {
        return porNombre.size();
    }

    public Collection<Artista> todos() {
        return Collections.unmodifiableCollection(porNombre.values());
    }

    public void limpiar() {
        porNombre.clear();
    }

    public List<Artista> obtenerTodos() {
        return new ArrayList<>(porNombre.values());
    }
}
