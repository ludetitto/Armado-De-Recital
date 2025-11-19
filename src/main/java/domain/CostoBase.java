package domain;

import java.util.logging.Logger;

public class CostoBase implements Costo {
	double costoBase;
	private final Logger logger = Logger.getLogger(CostoBase.class.getName());
	
	public CostoBase(double costoBase, Cancion cancion) {
		this.costoBase = costoBase;
	}

	@Override
	public double obtener() {
		 logger.info("El costo base es de $" + costoBase);
		 return costoBase;
	}
}
