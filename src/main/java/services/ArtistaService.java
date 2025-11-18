package services;

import domain.Artista;
import domain.Recital;
import domain.TipoRol;
import repository.ArtistaRepository;

import java.util.ArrayList;
import java.util.List;

public class ArtistaService {
	ArtistaRepository artistaRepository;

	public ArtistaService(ArtistaRepository artistaRepository) {
		this.artistaRepository = artistaRepository;
	}

	public ArtistaService() {
		// TODO Auto-generated constructor stub
	}

	void verGrafoColaboraciones() {
		// TODO: implementar
	}

	public boolean entrenarArtista(Artista artista, TipoRol rol) {
	    if (Recital.getInstance().estaContratado(artista)) {
	        return false;
	    }
	    if (!artista.puedeSerEntrenado()) {
	        return false;
	    }
	    artista.entrenarEn(rol);
	    return true;
	}

	public List<Artista> getArtistasBase() {
		List<Artista> artistas = artistaRepository.obtenerTodos(), contratados = new ArrayList<Artista>();

		for (Artista a : artistas) {
			if (a.esBase() ) {
				contratados.add(a);
			}
		}

		return contratados;
	}

	public List<Artista> getArtistasCandidatos() {
		List<Artista> artistas = artistaRepository.obtenerTodos(), candidatos = new ArrayList<Artista>();

		for (Artista a : artistas) {
			if (a.esExterno() && !Recital.getInstance().estaContratado(a)) {
				candidatos.add(a);
			}
		}

		return candidatos;
	}
}