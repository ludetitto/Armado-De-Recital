package repository;

import services.ArtistaService;
import services.CancionService;
import services.TipoRol;

public class ContratacionRepository {
	ArtistaService artista;
    CancionService cancion;
    TipoRol rol;
    double costoFinal;
    double descuentoAplicado;
    
	public ContratacionRepository(ArtistaService artista, CancionService cancion, TipoRol rol, double costoFinal,
			double descuentoAplicado) {
		super();
		this.artista = artista;
		this.cancion = cancion;
		this.rol = rol;
		this.costoFinal = costoFinal;
		this.descuentoAplicado = descuentoAplicado;
	}
	
	public ArtistaService getArtista() {
		return artista;
	}
	
	public CancionService getCancion() {
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
    
}
