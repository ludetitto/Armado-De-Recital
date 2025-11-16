package services;

import domain.Artista;
import domain.Cancion;

public abstract class CostoDecorator implements Costo {

	protected final Costo inner;
	protected static Cancion cancion;

    public CostoDecorator(Costo inner, Cancion cancion) {
        this.inner = inner;
        CostoDecorator.cancion = cancion;
    }

    @Override
    public double obtener() {
        return inner.obtener();
    }
    
    protected abstract double calcular(Artista artista);
}
