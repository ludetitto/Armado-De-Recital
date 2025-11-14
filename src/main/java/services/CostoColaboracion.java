package services;

import domain.Artista;

public class CostoColaboracion extends CostoDecorator {

	public CostoColaboracion(CostoBase inner) {
		super(inner);
	}

	@Override
	public double calcular(Artista artista) {
		// TODO Auto-generated method stub
		return 0;
	}

}
