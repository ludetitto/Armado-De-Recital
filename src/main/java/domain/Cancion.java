package domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cancion {
    private String titulo;
    private TipoEstado estado;
    private Map<TipoRol, Integer> rolesRequeridos;
    private Map<TipoRol, List<Artista>> asignaciones;

    public Cancion(String titulo, Map<TipoRol, Integer> rolesRequeridos) {
        this.titulo = Objects.requireNonNull(titulo, "titulo");
        this.rolesRequeridos = (rolesRequeridos != null)
                ? new EnumMap<>(rolesRequeridos)
                : new EnumMap<>(TipoRol.class);
        this.asignaciones = new EnumMap<>(TipoRol.class);
        this.estado = TipoEstado.BORRADOR;
        actualizarEstado();
    }
    
    // Necesario para importación json
    protected Cancion() {
    	this.rolesRequeridos = new EnumMap<>(TipoRol.class);
        this.asignaciones = new EnumMap<>(TipoRol.class);
        this.estado = TipoEstado.BORRADOR;
    }

    // GETTERS
    public String getTitulo() { return titulo; }

    @JsonIgnore
    public TipoEstado getEstado() { return estado; }

    public Map<TipoRol, Integer> getRolesRequeridos() {
        return Collections.unmodifiableMap(rolesRequeridos);
    }

    public Map<TipoRol, List<Artista>> getAsignaciones() {
        Map<TipoRol, List<Artista>> copia = new EnumMap<>(TipoRol.class);
        for (Map.Entry<TipoRol, List<Artista>> e : asignaciones.entrySet()) {
            copia.put(e.getKey(), Collections.unmodifiableList(new ArrayList<>(e.getValue())));
        }
        return Collections.unmodifiableMap(copia);
    }
    
    public List<Artista> getArtistasAsignados() {
        List<Artista> todos = new ArrayList<>();
        for (List<Artista> lista : asignaciones.values()) {
            todos.addAll(lista);
        }
        return Collections.unmodifiableList(todos);
    }
    
    public Set<TipoRol> getRolesCubiertos() {
        return Collections.unmodifiableSet(asignaciones.keySet());
    }

    public Map<TipoRol, Integer> getRolesFaltantes() {
        Map<TipoRol, Integer> faltantes = new EnumMap<>(TipoRol.class);

        for (Map.Entry<TipoRol, Integer> e : rolesRequeridos.entrySet()) {
            faltantes.put(e.getKey(), e.getValue());
        }
        for (Map.Entry<TipoRol, List<Artista>> e : asignaciones.entrySet()) {
            int asignados = e.getValue() == null ? 0 : e.getValue().size();
            if (asignados == 0) continue;
            TipoRol rol = e.getKey();
            faltantes.computeIfPresent(rol, (k, v) -> {
                int r = v - asignados;
                
                return (r > 0) ? r : null;
            });
        }
        return faltantes;
    }

    // SETTERS
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public void setEstado(TipoEstado estado) { this.estado = estado; }

    public void setRolesRequeridos(Map<TipoRol, Integer> rolesCargados) {
        if (rolesCargados != null) {
            this.rolesRequeridos = new EnumMap<>(rolesCargados);
        } else {
            this.rolesRequeridos = new EnumMap<>(TipoRol.class);
        }
    }
    
    public void setAsignaciones(Map<TipoRol, List<Artista>> asignacionesCargadas) {
        if (asignacionesCargadas == null) {
            this.asignaciones = new EnumMap<>(TipoRol.class);
        } else {
            // copiamos a EnumMap y garantizamos listas mutables internas
            EnumMap<TipoRol, List<Artista>> m = new EnumMap<>(TipoRol.class);
            for (Map.Entry<TipoRol, List<Artista>> e : asignacionesCargadas.entrySet()) {
                m.put(e.getKey(), new ArrayList<>(e.getValue()));
            }
            this.asignaciones = m;
        }
        actualizarEstado();
    }

    
    // UTILS
    public boolean estaCompleta() {
        return getRolesFaltantes().isEmpty();
    }

    private void actualizarEstado() {
        if (asignaciones.isEmpty()) {
            estado = TipoEstado.BORRADOR;
        } else if (estaCompleta()) {
            estado = TipoEstado.COMPLETA;
        } else {
            estado = TipoEstado.INCOMPLETA;
        }
    }
    
    public List<TipoRol> verRoles() {
        List<TipoRol> roles = new ArrayList<>();
        getAsignaciones().forEach((rol, artistas) -> roles.add(rol));
        return roles;
    }

    public Map<TipoRol, Integer> verRolesFaltantes() {
        Map<TipoRol, Integer> faltantes = new LinkedHashMap<>();

        for (Map.Entry<TipoRol, Integer> entry : getRolesRequeridos().entrySet()) {
            faltantes.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }

        for (TipoRol rolCubierto : verRoles()) {
            faltantes.computeIfPresent(rolCubierto, (k, v) -> v > 1 ? v - 1 : null);
        }

        return faltantes;
    }
    
    
    public void asignarArtista(Artista artista, TipoRol rol) {
        Objects.requireNonNull(artista, "artista");
        Objects.requireNonNull(rol, "rol");
        if (!artista.puedeOcuparRol(rol)) {
            throw new IllegalArgumentException(artista.getNombre() + " no puede ocupar el rol: " + rol);
        }
        asignaciones.computeIfAbsent(rol, k -> new ArrayList<>()).add(artista);
        actualizarEstado();
    }

    public void desasignarArtista(Artista artista, TipoRol rol) {
        Objects.requireNonNull(artista, "artista");
        Objects.requireNonNull(rol, "rol");
        List<Artista> artistasEnRol = asignaciones.get(rol);
        if (artistasEnRol != null) {
            artistasEnRol.remove(artista);
            if (artistasEnRol.isEmpty()) {
                asignaciones.remove(rol);
            }
        }
        actualizarEstado();
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - Roles requeridos: %s", titulo, estado, rolesRequeridos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancion)) return false;
        Cancion cancion = (Cancion) o;
        return Objects.equals(titulo, cancion.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }
}
