package services;

public class CostoDecorator implements Costo {

	protected final Costo inner;

    public CostoDecorator(Costo inner) {
        this.inner = inner;
    }

    @Override
    public double calcularExtra() {
        return inner.calcularExtra();
    }

}
