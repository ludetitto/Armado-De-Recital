package discografica;

import java.util.Set;

public class Artista {
    String nombre;
    Set<TipoRol> roles;
    Set<String> bandas;
    double costo;
    int maxCanciones;

    @Override
    public String toString() {
        return nombre + " | roles: " + joinRoles() + " | costo: " + costo + " | max: " + maxCanciones;
    }

    private String joinRoles() {
        if (roles == null || roles.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (TipoRol r : roles) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(r.name());
        }
        return sb.toString();
    }

}
