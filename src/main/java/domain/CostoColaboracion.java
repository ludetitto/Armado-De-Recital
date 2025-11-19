package domain;

import java.util.logging.Logger;

public class CostoColaboracion extends CostoDecorator {

	private final Logger logger = Logger.getLogger(CostoColaboracion.class.getName());
	public CostoColaboracion(Costo inner, Cancion cancion, Artista artista) { 
        super(inner, cancion, artista); 
    }

    @Override
    public double obtener() {
        
        double costoPrevio = inner.obtener(); 
        boolean tieneColaboracion = false;
        double costoColaboracion = costoPrevio;
        
        for(Artista a : cancion.getArtistasAsignados()) {
            if(this.artista != a && this.artista.compartioBanda(a)) { 
                tieneColaboracion = true;
                break; 
            }
        }
        
        if (tieneColaboracion) {
            costoColaboracion = costoPrevio * 0.5;
        }
        
        logger.info("El costo bajo colaboración pasa de $" + costoPrevio + " a $" + costoColaboracion);
        
        return costoColaboracion;
    }

}
