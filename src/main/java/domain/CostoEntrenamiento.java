package domain;

import java.util.logging.Logger;

public class CostoEntrenamiento extends CostoDecorator {

	private final Logger logger = Logger.getLogger(CostoEntrenamiento.class.getName());
	
	public CostoEntrenamiento(Costo inner, Cancion cancion, Artista artista) { 
		super(inner, cancion, artista);
	}

	@Override
	public double obtener() {
        
		double costoPrevio = inner.obtener(); 
        
        boolean tieneEntrenamiento = this.artista.getRoles().values().stream()
                .anyMatch(e -> e == EstadoRol.ENTRENAMIENTO);
        
        double costoEntrenamiento = costoPrevio;
        
        if (tieneEntrenamiento) {
        	costoEntrenamiento = costoPrevio * 1.5; 
        }
        
        logger.info("El costo con entrenamiento pasa de $" + costoPrevio + " a $" + costoEntrenamiento);

        return costoEntrenamiento;
	}
}
