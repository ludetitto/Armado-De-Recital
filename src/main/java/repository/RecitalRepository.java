package repository;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import domain.Recital;

public class RecitalRepository {
    private static RecitalRepository instance;
    //@JacksonXmlProperty(localName = "recital")
    private Recital recital;
    
    private RecitalRepository() {
        this.recital = Recital.getInstance();
    }
    
    public static RecitalRepository getInstance() {
        if (instance == null) {
            instance = new RecitalRepository();
        }
        return instance;
    }
    
    public static void setRecitalInstance(Recital recitalCargado) {
        getInstance().recital = recitalCargado;
    }
    
    public Recital getRecital() {
        return recital;
    }

}