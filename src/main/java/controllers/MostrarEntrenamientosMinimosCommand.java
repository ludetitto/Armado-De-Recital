package controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

import domain.Artista;
import domain.Recital;
import repository.ArtistaRepository;
import repository.CancionRepository;
import services.PrologService;

public class MostrarEntrenamientosMinimosCommand implements ComandoContratacion {
	private final PrologService prologService;

	public MostrarEntrenamientosMinimosCommand(PrologService prologService) {
		this.prologService = prologService;
	}
	
	@Override
	public void ejecutar() {
		ArtistaRepository artistaRepository = new ArtistaRepository();
		CancionRepository cancionRepository = new CancionRepository();
		
		Recital recital = Recital.getInstance();
		Set<String> nombresArtistas = recital.getArtistas().stream()
			    .map(Artista::getNombre)
			    .collect(Collectors.toSet());
		
        if (nombresArtistas.isEmpty()) {
            System.out.println("El recital aún no tiene artistas cargados.");
            return;
        }
        
        try {
			prologService.entrenamientosMinimos(artistaRepository, cancionRepository, nombresArtistas);
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
