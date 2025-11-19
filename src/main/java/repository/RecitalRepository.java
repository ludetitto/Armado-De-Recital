package repository;

import domain.Recital;

public class RecitalRepository {
    private Recital recital;
    
    public RecitalRepository(Recital recital) {
		this.recital = recital;
	}

	public void setRecital(Recital recital) {
		this.recital = recital;
	}


	public Recital getRecital() {
        return recital;
    }

}