package services;

import java.util.ArrayList;
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
	private RecitalService recitalService;
	
	public ContratacionService(CancionService cancionService, ArtistaService artistaService) {
		this.cancionService = cancionService;
		this.artistaService = artistaService;
	}
	
	public ContratacionService(RecitalService recitalService, ArtistaService artistaService) {
		this.artistaService = artistaService;
		this.recitalService = recitalService;
	}

	public void generarContratacion(Artista artista, Cancion cancion, TipoRol rol) {
        if (artistaYaContratadoEnRecital(artista)) {
            throw new IllegalStateException("El artista " + artista.getNombre() + " ya está contratado en el recital");
        }

        Recital.getInstance().agregarContratacion(new Contratacion(artista, cancion, rol, obtenerCosto(artista), 0));
    }
    
    private boolean artistaYaContratadoEnRecital(Artista artista) {
        return Recital.getInstance().getContrataciones()
                .stream()
                .anyMatch(c -> c.getArtista().equals(artista));
    }

    public double obtenerCosto(Artista artista) {
        Costo costo = new CostoBase(artista.getCostoBase());
        Costo costoConEntrenamientos = new CostoEntrenamiento(costo);
        return costoConEntrenamientos.obtener();
    }
    
    public void contratarArtistas(Cancion cancion) {
        Map<TipoRol, Integer> faltantes = new LinkedHashMap<>(cancionService.verRolesFaltantes(cancion));
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
                    try {
                        cancion.asignarArtista(a, rol);
                        generarContratacion(a, cancion, rol);
                        cubiertos++;
                        System.out.println(" + Asignado: " + a.getNombre() + " -> " + rol);
                    } catch (IllegalArgumentException ex) {
                    }
                }
            }

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
}