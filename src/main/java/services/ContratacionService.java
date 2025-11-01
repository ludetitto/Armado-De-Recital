package services;

public class ContratacionService {  

	private int id;
    public ContratacionService() {} // Para Jackson
    public ContratacionService(int id) { this.id = id; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
}
