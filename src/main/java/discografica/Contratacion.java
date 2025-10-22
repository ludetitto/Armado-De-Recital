package discografica;

public class Contratacion {
    Artista artista;
    Cancion cancion;
    TipoRol rol;
    double costoFinal;
    boolean descuentoAplicado;
    
    @Override
    public String toString() {
        String n = (artista == null ? "-" : artista.nombre);
        String c = (cancion == null ? "-" : cancion.titulo);
        String r = (rol == null ? "-" : rol.name());
        return n + " -> " + c + " (" + r + ") | costo: " + costoFinal + (descuentoAplicado ? " [desc]" : "");
    }

}
