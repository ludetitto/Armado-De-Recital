package services;

import domain.Artista;

public class CostoColaboracion extends CostoDecorator {

	public CostoColaboracion(CostoBase inner) {
		super(inner, cancion);
	}

	@Override
	public double calcular(Artista artista) {
		double costo = inner.obtener();
		boolean tieneColaboracion = false;
		double costoColaboracion = costo;
	       
		for(Artista a : Costo.cancion.getArtistasAsignados()) {
			if(artista.compartioBanda(a))
				tieneColaboracion = true;
		}
		
	    if (tieneColaboracion) {
	    	costoColaboracion = costo * 0.5;
	    }

	    return costoColaboracion;
	}

}
