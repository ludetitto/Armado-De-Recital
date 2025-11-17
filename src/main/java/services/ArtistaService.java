package services;

import domain.Artista;
import domain.Contratacion;
import domain.EstadoRol;
import domain.Recital;
import domain.TipoRol;
import repository.ArtistaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArtistaService {
	ArtistaRepository artistaRepository;

	public ArtistaService(ArtistaRepository artistaRepository) {
		this.artistaRepository = artistaRepository;
	}

	public ArtistaService() {
		// TODO Auto-generated constructor stub
	}

	List<TipoRol> verRoles(Artista artista) {
		List<TipoRol> roles = new ArrayList<>();
		Map<TipoRol, EstadoRol> rolesDelArtista = artista.getRoles();

		for (Map.Entry<TipoRol, EstadoRol> entry : rolesDelArtista.entrySet()) {
			EstadoRol estado = entry.getValue();
			if (estado == EstadoRol.BASE || estado == EstadoRol.ENTRENAMIENTO) {
				roles.add(entry.getKey());
			}
		}
		return roles;
	}

	Boolean puedeOcuparRol(Artista artista, TipoRol rol) {
		return artista.puedeOcuparRol(rol);
	}

	void verGrafoColaboraciones() {
		// TODO: implementar
	}

	public void recibirEntrenamiento(Artista artista, TipoRol rolAgregado) {
		if (!artista.puedeOcuparRol(rolAgregado) && artista.esExterno() && !estaContratado(artista)) {
			artista.agregarRol(rolAgregado);
			System.out.println("Entrenamiento aplicado: " + artista.getNombre() + " ahora puede " + rolAgregado);
		} else
			System.out.println("El artista " + artista.getNombre() + " ya puede ocupar el rol " + rolAgregado);
	}

	private boolean estaContratado(Artista artista) {
		for (Contratacion c : Recital.getInstance().getContrataciones()) {
			if (c.getArtista().equals(artista)) {
				return true;
			}
		}
		return false;
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

			List<Contratacion> contrataciones = Recital.getInstance().getContrataciones();

			boolean estaContratado = false;
			
			for (Contratacion contratacion : contrataciones) {
				if(contratacion.getArtista().equals(a)) {
					estaContratado = true;
				}
			}
			
			if (a.esExterno() && !estaContratado) {
				candidatos.add(a);
			}
		}

		return candidatos;
	}
}