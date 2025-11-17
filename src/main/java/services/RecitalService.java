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

	
    public Map<TipoRol, Integer> verRolesFaltantes(Recital recital) {
        return recital.getRolesFaltantesTotal();
    }

	
//	public Map<TipoRol, Integer> verRolesFaltantes() {
//	    return Recital.getInstance().getRolesFaltantesTotal();
//	}
	
    List<Artista> verArtistasContratados(Recital recital) {
        List<Contratacion> contrataciones = recital.getContrataciones();
        return contrataciones.stream().map(Contratacion::getArtista).toList();
    }

    Set<Cancion> verCanciones(Recital recital) {
        return recital.getCanciones();
    }

    public void cargarEstadoInicial(String archivo) {}
    
    public void reportarEstadoActual(RecitalRepository repositorioActual, Path rutaJsonSalida) {
    	FuenteRecital fuenteSalida = new JsonFuenteRecital(rutaJsonSalida);
		fuenteSalida.guardar(repositorioActual);
    }
}
