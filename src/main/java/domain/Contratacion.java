package domain;

import java.util.*;

public class Contratacion implements Comparable<Contratacion> {
    private Artista artista;
    private Cancion cancion;
    private TipoRol rol;
    private double costoFinal;
    
    public Contratacion(Artista artista, Cancion cancion, TipoRol rol, 
                        double costoFinal) {
        this.artista = artista;
        this.cancion = cancion;
        this.rol = rol;
        this.costoFinal = costoFinal;
    }
    
    // Se necesita para importación json
    protected Contratacion() {
    	
    }
    
    // GETTERS
    public Artista getArtista() { 
    	return artista; 
    }
    
    public Cancion getCancion() { 
    	return cancion; 
    }
    
    public TipoRol getRol() { 
    	return rol; 
    }
    
    public double getCostoFinal() { 
    	return costoFinal; 
    }
    
    // UTILS
    @Override
    public String toString() {
        return String.format("%s → %s (%s) - Costo: $%.2f",
            artista.getNombre(), cancion.getTitulo(), rol, costoFinal);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contratacion that = (Contratacion) o;
        return artista.equals(that.artista) && 
               cancion.equals(that.cancion) && 
               rol == that.rol;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(artista, cancion, rol);
    }

	@Override
	public int compareTo(Contratacion o) {
		if (costoFinal > o.costoFinal)
			return 1;
		else {
			if(costoFinal < o.costoFinal)
				return -1;
			else
				return 0;
		}
	}
}