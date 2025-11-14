package services;

import domain.Artista;
import domain.EstadoRol;

public class CostoEntrenamiento extends CostoDecorator {

	public CostoEntrenamiento(Costo inner) {
		super(inner);
	}

	@Override
	public double calcular(Artista artista) {
		double costo = inner.obtener();
        boolean tieneEntrenamiento = artista.getRoles().values().stream()
                .anyMatch(e -> e == EstadoRol.ENTRENAMIENTO);
        double costoEntrenamiento = costo;
        
        if (tieneEntrenamiento) {
        	costoEntrenamiento = costo * 1.5;
        }

        return costoEntrenamiento;
	}
}
