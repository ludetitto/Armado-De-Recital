package discografica;

import java.util.List;
import java.util.Map;

public class Cancion {
    String titulo;
    Map<TipoRol, Integer> rolesRequeridos;
    TipoEstado estado;
    Map<TipoRol, List<Artista>> asignaciones;

    @Override
    public String toString() {
        return titulo + " | req: " + reqToString() + " | estado: " + (estado == null ? "-" : estado.name());
    }

    private String reqToString() {
        if (rolesRequeridos == null || rolesRequeridos.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (java.util.Map.Entry<TipoRol, Integer> e : rolesRequeridos.entrySet()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.getKey().name()).append("x").append(e.getValue());
        }
        return sb.toString();
    }

}
