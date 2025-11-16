package services;

import domain.Artista;

public abstract class CostoDecorator implements Costo {

	protected final Costo inner;

    public CostoDecorator(Costo inner) {
        this.inner = inner;
    }

    @Override
    public double obtener() {
        return inner.obtener();
    }
    
    public abstract double calcular(Artista artista);
}
