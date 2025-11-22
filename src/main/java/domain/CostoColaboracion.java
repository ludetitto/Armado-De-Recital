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
        double costoColaboracion = costoPrevio;
        
        if (artista.compartioBanda(cancion)) {
            costoColaboracion = costoPrevio * 0.5;
        }
        
        logger.info("El costo bajo colaboración pasa de $" + costoPrevio + " a $" + costoColaboracion);
        
        return costoColaboracion;
    }

}
