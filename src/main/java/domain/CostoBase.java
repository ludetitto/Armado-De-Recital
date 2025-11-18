package domain;

public class CostoBase implements Costo {
	double costoBase;
	
	public CostoBase(double costoBase, Cancion cancion) {
		this.costoBase = costoBase;
	}

	@Override
	public double obtener() {
		return costoBase;
	}
}
