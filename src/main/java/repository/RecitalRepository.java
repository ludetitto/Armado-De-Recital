package repository;

import domain.Recital;

public class RecitalRepository {
    private static RecitalRepository instance;
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
    
    public Recital getRecital() {
        return recital;
    }
}