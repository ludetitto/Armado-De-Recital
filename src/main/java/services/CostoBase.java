package services;

public class CostoBase implements Costo {
	double costoBase;

	public CostoBase(double costoBase) {
		this.costoBase = costoBase;
	}
	
	@Override
	public double obtener() {
		return costoBase;
	}
}
