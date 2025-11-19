package domain;

// Decorator sobre precio final de contratación
public abstract class CostoDecorator implements Costo {

	protected final Costo inner;
    protected final Cancion cancion; 
    protected final Artista artista;

    public CostoDecorator(Costo inner, Cancion cancion, Artista artista) {
        this.inner = inner;
        this.cancion = cancion;
        this.artista = artista;
    }

    @Override
    public abstract double obtener();
}
