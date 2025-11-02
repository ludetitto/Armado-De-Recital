package services;

import domain.Artista;
import domain.EstadoRol;

public class CostoEntrenamiento extends CostoDecorator {

	private final Artista artista;

	public CostoEntrenamiento(Costo inner, Artista artista) {
        super(inner);
        this.artista = artista;
    }

    @Override
    public double calcularExtra() {
    	double extraAnterior = inner.calcularExtra();
        boolean tieneEntrenamiento = artista.getRoles().values().stream()
                .anyMatch(e -> e == EstadoRol.ENTRENAMIENTO);
        double extraEntrenamiento = 0;
        
        if (tieneEntrenamiento) {
            extraEntrenamiento = artista.getCostoBase() * 0.5;
        }

        return extraAnterior + extraEntrenamiento;
    }

}
