package controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import services.PrologService;

public class MostrarEntrenamientosMinimosCommand implements ComandoContratacion {
	private final PrologService prologService;
	private final double costo;

	public MostrarEntrenamientosMinimosCommand(PrologService prologService, double costo) {
		this.prologService = prologService;
		this.costo = costo;
	}
	
	@Override
	public void ejecutar() {
		
        try {
        	int entrenamientos = prologService.entrenamientosMinimos();
			System.out.println("Entrenamientos mínimos requeridos: " + entrenamientos);
			System.out.println("Costo total de entrenamientos: " + entrenamientos * costo);
        } catch (IOException e) {
        	System.out.println("Error: No fue posible acceder a la base de conocimiento.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Error: No fue posible acceder a la base de conocimiento.");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.out.println("Error: No fue posible acceder a la base de conocimiento.");
			e.printStackTrace();
		}
	}

	@Override
	public void deshacer() {
	}
}