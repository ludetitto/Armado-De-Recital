package services;

import domain.Artista;

public abstract class CostoDecorator implements Costo {

	protected final CostoBase inner;

    public CostoDecorator(CostoBase inner) {
        this.inner = inner;
    }

    @Override
    public double obtener() {
        return inner.obtener();
    }
    
    public abstract double calcular(Artista artista);
}
