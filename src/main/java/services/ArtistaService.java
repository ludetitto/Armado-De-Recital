package services;

import domain.Artista;
import domain.Recital;
import repository.ArtistaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ArtistaService {
	
	ArtistaRepository artistaRepository;
	private final Logger logger = Logger.getLogger(ArtistaService.class.getName());
	
	public ArtistaService(ArtistaRepository artistaRepository) {
		this.artistaRepository = artistaRepository;
	}
	
	// GETTERS
	public List<Artista> getArtistasBase() {
		List<Artista> artistas = artistaRepository.obtenerTodos(), contratados = new ArrayList<Artista>();

		for (Artista a : artistas) {
			if (a.esBase() ) {
				contratados.add(a);
			}
		}
		
		logger.info("Los artistas contratados solicitados son " + contratados);

		return contratados;
	}

	public List<Artista> getArtistasCandidatos() {
		List<Artista> artistas = artistaRepository.obtenerTodos(), candidatos = new ArrayList<Artista>();

		for (Artista a : artistas) {
			if (a.esExterno() && !Recital.getInstance().estaContratado(a)) {
				candidatos.add(a);
			}
		}
		
		logger.info("Los artistas candidatos solicitados son " + candidatos);

		return candidatos;
	}
	
	public int getCantidadDeCancionesQueToca(Artista artista) {
		return (int) Recital.getInstance().getContrataciones().stream()
        .filter(c -> c.getArtista().equals(artista))
        .count();
	}
}