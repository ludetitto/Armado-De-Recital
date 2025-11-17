package controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

import domain.Artista;
import domain.Recital;
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
		Recital recital = Recital.getInstance();
		Set<String> nombresArtistas = recital.getArtistas().stream()
			    .map(Artista::getNombre)
			    .collect(Collectors.toSet());
		
        if (nombresArtistas.isEmpty()) {
            System.out.println("El recital aún no tiene artistas cargados.");
            return;
        }
        
        try {
        	int entrenamientos = prologService.entrenamientosMinimos(nombresArtistas);
			System.out.println("Entrenamientos mínimos requeridos: " + entrenamientos);
			System.out.println("Costo total de entrenamientos: " + entrenamientos * costo);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void deshacer() {
		// TODO Auto-generated method stub
		
	}

}
