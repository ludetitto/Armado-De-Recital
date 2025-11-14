package services;

import domain.Artista;
import domain.EstadoRol;

public class CostoEntrenamiento extends CostoDecorator {

	public CostoEntrenamiento(CostoBase inner) {
		super(inner);
	}

	@Override
	public double calcular(Artista artista) {
		double costo = inner.obtenerCostoBase();
        boolean tieneEntrenamiento = artista.getRoles().values().stream()
                .anyMatch(e -> e == EstadoRol.ENTRENAMIENTO);
        double costoEntrenamiento = 0;
        
        if (tieneEntrenamiento) {
        	costoEntrenamiento = costo * 1.5;
        }

        return costoEntrenamiento;
	}
}
