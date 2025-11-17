package repository;

import domain.Recital;

public class RecitalRepository {
    //@JacksonXmlProperty(localName = "recital")
    private Recital recital;
    
    public RecitalRepository(Recital recital) {
		this.recital = recital;
	}

	public Recital getRecital() {
        return recital;
    }

}