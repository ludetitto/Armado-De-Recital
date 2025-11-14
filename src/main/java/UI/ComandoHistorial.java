package UI;

import java.util.ArrayList;
import java.util.List;

public class ComandoHistorial {

    private final List<String> historial = new ArrayList<>();

    public void agregar(String linea) {
        if (linea != null && !linea.isBlank()) historial.add(linea);
    }

    public List<String> ver() {
        return List.copyOf(historial);
    }

    public int cantidadEntradas() {
        return historial.size();
    }

    @Override
    public String toString() {
        return "Historial(" + historial.size() + " items)";
    }
}