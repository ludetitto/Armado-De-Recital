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
    
    public void descontratar(String nombreArtista) {
    	Contratacion contratacion = Recital.getInstance().getContrataciones().stream()
                .filter(c -> c.getArtista().getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);
        if (contratacion == null) {
            System.out.println("Error: Contratacion no encontrado");
            return;
        }
    	Recital.getInstance().eliminarContratacion(contratacion);
    }
    
    public void designarArtistaDeCancion(String tituloCancion, String nombreArtista, TipoRol rol) {
    	Cancion cancion = Recital.getInstance().getCanciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(tituloCancion))
                .findFirst().orElse(null);
        if (cancion == null) {
            System.out.println("Error: canción no encontrada: " + tituloCancion);
            return;
        }
        Artista artista = Recital.getInstance().getArtistas().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreArtista))
                .findFirst().orElse(null);

        if (artista == null) {
            System.out.println("Error: artista no encontrado: " + nombreArtista);
            return;
        }
        
        cancion.desasignarArtista(artista, rol);
        
        Recital.getInstance().designarArtistadeCancion(cancion);
        
        
    }

    public void cargarEstadoInicial(String archivo) {}
    
    public void reportarEstadoActual(RecitalRepository repositorioActual, Path rutaJsonSalida) {
    	FuenteRecital fuenteSalida = new JsonFuenteRecital(rutaJsonSalida);
		fuenteSalida.guardar(repositorioActual);
    }


}
