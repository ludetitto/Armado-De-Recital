package services;

import domain.Artista;
import domain.Cancion;
import domain.Contratacion;
import domain.Recital;
import domain.TipoRol;
import repository.FuenteRecital;
import repository.JsonFuenteRecital;
import repository.RecitalRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecitalService {

	// UTILS
	public Map<TipoRol, Integer> verRolesFaltantes(Recital recital) {
		return recital.getRolesFaltantesTotal();
	}

	List<Artista> verArtistasContratados(Recital recital) {
		List<Contratacion> contrataciones = recital.getContrataciones();
		return contrataciones.stream().map(Contratacion::getArtista).toList();
	}

	Set<Cancion> verCanciones(Recital recital) {
		return recital.getCanciones();
	}

	public void descontratar(String nombreArtista, String tituloCancion) {

		Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);
		Artista artista = Recital.getInstance().obtenerArtistaPorNombre(nombreArtista);

		if (cancion == null) {
			System.out.println("Error: canción no encontrada: " + tituloCancion);
			return;
		}

		if (artista == null) {
			System.out.println("Error: artista no encontrado: " + nombreArtista);
			return;
		}

		Contratacion contratacion = Recital.getInstance().obtenerContratacionPorArtistaYCancion(cancion, artista);

		if (contratacion == null) {
			System.out.println("Error: Contratacion no encontrado");
			return;
		}
		Recital.getInstance().eliminarContratacion(contratacion);
	}

	public void designarArtistaDeCancion(String tituloCancion, String nombreArtista, TipoRol rol) {
    	Cancion cancion = Recital.getInstance().obtenerCancionPorNombre(tituloCancion);
    	
    	Artista artista = Recital.getInstance().obtenerArtistaPorNombre(nombreArtista);
        
    	if (cancion == null) {
            System.out.println("Error: canción no encontrada: " + tituloCancion);
            return;
    	}
    	
        if (artista == null) {
            System.out.println("Error: artista no encontrado: " + nombreArtista);
            return;
        }
        
        cancion.desasignarArtista(artista, rol);
        
        Recital.getInstance().designarArtistadeCancion(cancion);
        
    }

	public void cargarEstadoInicial(String archivo) {
	}

	public void reportarEstadoActual(RecitalRepository repositorioActual, Path rutaJsonSalida) {
		FuenteRecital fuenteSalida = new JsonFuenteRecital(rutaJsonSalida);
		fuenteSalida.guardar(repositorioActual);
	}

}
