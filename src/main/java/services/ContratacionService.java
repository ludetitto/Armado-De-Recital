package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import domain.Artista;
import domain.Cancion;
import domain.Contratacion;
import domain.Recital;
import domain.TipoRol;

public class ContratacionService {

	private CancionService cancionService;
	private ArtistaService artistaService;
	
	public ContratacionService(CancionService cancionService, ArtistaService artistaService) {
		this.cancionService = cancionService;
		this.artistaService = artistaService;
	}
	
	public ContratacionService(RecitalService recitalService, ArtistaService artistaService) {
		this.artistaService = artistaService;
	}

	public void generarContratacion(Artista artista, Cancion cancion, TipoRol rol) {
        if (artistaYaContratadoEnRecital(artista)) {
            throw new IllegalStateException("El artista " + artista.getNombre() + " ya está contratado en el recital");
        }

        Recital.getInstance().agregarContratacion(new Contratacion(artista, cancion, rol, obtenerCosto(cancion, artista), 0));
    }
    
    private boolean artistaYaContratadoEnRecital(Artista artista) {
        return Recital.getInstance().getContrataciones()
                .stream()
                .anyMatch(c -> c.getArtista().equals(artista));
    }

    public double obtenerCosto(Cancion cancion, Artista artista) {
        Costo costo = new CostoBase(artista.getCostoBase(), cancion);
        Costo costoConEntrenamientos = new CostoEntrenamiento(costo);
        return costoConEntrenamientos.obtener();
    }
    
    public void contratarArtistas(Cancion cancion) {
        Map<TipoRol, Integer> faltantes = new LinkedHashMap<>(cancionService.verRolesFaltantes(cancion));
        List<Contratacion> posibles = new ArrayList<Contratacion>();
        
        if (faltantes.isEmpty()) {
            System.out.println("La canción '" + cancion.getTitulo() + "' ya tiene todos los roles cubiertos.");
            return;
        }

        List<Artista> candidatos = new ArrayList<>(artistaService.getArtistasCandidatos());
        Map<TipoRol, List<Artista>> actuales = cancion.getAsignaciones();

        System.out.println("--- Contratación automática para '" + cancion.getTitulo() + "' ---");

        for (Map.Entry<TipoRol, Integer> e : faltantes.entrySet()) {
            TipoRol rol = e.getKey();
            int necesarios = e.getValue();
            int cubiertos = 0;

            List<Artista> ya = actuales.getOrDefault(rol, List.of());

            for (Artista a : candidatos) {
                if (cubiertos >= necesarios) break;

                boolean yaEnCancion = ya.contains(a);
                boolean yaEnRecital = artistaYaContratadoEnRecital(a);
                
                if (a.puedeOcuparRol(rol) && !yaEnCancion && !yaEnRecital) {
                	posibles.add(new Contratacion(a, cancion, rol, obtenerCosto(cancion, a), 0));
                }
            }
            
            if(posibles.isEmpty()) {
            	System.out.println("No hay artistas disponibles para contratar para el rol " + rol);
            	return;
            }
            
            Contratacion elegido = Collections.min(posibles);
        	
            contratarArtista(cancion, elegido.getArtista(), rol);
        	cubiertos++;
            System.out.println(" + Asignado: " + elegido.getArtista().getNombre() + " -> " + rol);

            if (cubiertos < necesarios) {
                System.out.println(" ! No se pudo cubrir completamente " + rol + " (faltaron "
                        + (necesarios - cubiertos) + ")");
            }
        }

        Map<TipoRol, Integer> remanente = cancionService.verRolesFaltantes(cancion);
        if (remanente.isEmpty()) {
            System.out.println("Resultado: ¡Roles cubiertos!");
        } else {
            System.out.println("Resultado: aún faltan roles:");
            remanente.forEach((r, n) -> System.out.println(" - " + r + ": " + n));
        }
    }
    
    private void contratarArtista(Cancion cancion, Artista artista, TipoRol rol) {
    	try {
            cancion.asignarArtista(artista, rol);
            generarContratacion(artista, cancion, rol);
        } catch (IllegalArgumentException ex) {
        }
    }
    
    public void contratarArtistasRecital() {
    	for(Cancion c : Recital.getInstance().getCanciones()) {
    		contratarArtistas(c);
    	}
    }
}