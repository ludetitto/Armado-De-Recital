package repository;

import services.TipoRol;

public class ContratacionRepository {
	ArtistaRepository artista;
	CancionRepository cancion;
    public TipoRol rol;
    private double costoFinal;
    public double descuentoAplicado;
    
    public ContratacionRepository() {
        // para json
    }
    
	public ContratacionRepository(ArtistaRepository artista, CancionRepository cancion, TipoRol rol, double costoFinal,
			double descuentoAplicado) {
		this.artista = artista;
		this.cancion = cancion;
		this.rol = rol;
		this.costoFinal = costoFinal;
		this.descuentoAplicado = descuentoAplicado;
	}
	
	public ArtistaRepository getArtista() {
		return artista;
	}
	
	public CancionRepository getCancion() {
		return cancion;
	}
	
	public TipoRol getRol() {
		return rol;
	}
	
	public double getCostoFinal() {
		return costoFinal;
	}
	
	public double getDescuentoAplicado() {
		return descuentoAplicado;
	}

	public void setArtista(ArtistaRepository artista) {
		this.artista = artista;
	}

	public void setCancion(CancionRepository cancion) {
		this.cancion = cancion;
	}

	public void setRol(TipoRol rol) {
		this.rol = rol;
	}

	public void setCostoFinal(double costoFinal) {
		this.costoFinal = costoFinal;
	}

	public void setDescuentoAplicado(double descuentoAplicado) {
		this.descuentoAplicado = descuentoAplicado;
	}
	
	
    
}
