package services;

public class CostoBase implements Costo {
	double costoBase;

	public CostoBase(double costoBase) {
		this.costoBase = costoBase;
	}
	
	@Override
	public double obtener() {
		return 0;
	}
	
	public double obtenerCostoBase() {
		return costoBase;
	}

}
