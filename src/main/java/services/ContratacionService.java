package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import domain.Artista;
import domain.Cancion;
import domain.Contratacion;
import domain.Costo;
import domain.CostoBase;
import domain.CostoColaboracion;
import domain.CostoEntrenamiento;
import domain.Recital;
import domain.TipoRol;

public class ContratacionService {
	
	private final Logger logger = Logger.getLogger(ArtistaService.class.getName());
	
	public void generarContratacion(Artista artista, Cancion cancion, TipoRol rol) {
        Recital.getInstance().agregarContratacion(new Contratacion(artista, cancion, rol, obtenerCosto(cancion, artista)));
        cancion.asignarArtista(artista, rol);
    }
    
	// UTILS
    private boolean artistaYaContratadoEnCancion(Artista artista, Cancion cancion) {
        return Recital.getInstance().estaContratadoEnCancion(artista, cancion);
    }

    public double obtenerCosto(Cancion cancion, Artista artista) {
        Costo costo = new CostoBase(artista.getCostoBase(), cancion);
        Costo costoConEntrenamientos = new CostoEntrenamiento(costo, cancion, artista);
        Costo costoConColaboracion = new CostoColaboracion(costoConEntrenamientos, cancion, artista);
        return costoConColaboracion.obtener();
    }
    
    public void contratarArtistas(Cancion cancion) {
        Map<TipoRol, Integer> faltantes = new LinkedHashMap<>(cancion.verRolesFaltantes());

        if (faltantes.isEmpty()) {
            System.out.println("La canción '" + cancion.getTitulo() + "' ya tiene todos los roles cubiertos.");
            return;
        }

        List<Artista> candidatos = new ArrayList<>(Recital.getInstance().getArtistas());

        System.out.println("--- Contratación automática para '" + cancion.getTitulo() + "' ---");

        for (Map.Entry<TipoRol, Integer> e : faltantes.entrySet()) {
            TipoRol rol = e.getKey();
            int necesarios = e.getValue();
            int cubiertos = 0;
            boolean pararCiclo = false;

            while (cubiertos < necesarios && !pararCiclo) {
                List<Contratacion> posibles = new ArrayList<>();

                for (Artista a : candidatos) {
                    if (a.puedeOcuparRol(rol) && !artistaYaContratadoEnCancion(a, cancion) && a.getCantidadDispuestoATocar() > 0) {
                        posibles.add(new Contratacion(a, cancion, rol, obtenerCosto(cancion, a)));
                        logger.info("Se agregó el artista " + a + " para el rol " + rol + " de la cancion " + cancion + " bajo un costo de $" + obtenerCosto(cancion, a));
                    }
                }

                if (!posibles.isEmpty()) {
                // CRITERIO: Elegir el más barato
                Contratacion elegido = Collections.min(posibles);
                contratarArtista(cancion, elegido.getArtista(), rol);
                cubiertos++;
                
                System.out.println(" + Asignado: " + elegido.getArtista().getNombre() + 
                                   " -> " + rol + " (costo: " + elegido.getCostoFinal() + ")");
                }
                else {
                	System.out.println(" ! No hay más artistas disponibles para " + rol + 
                            " (cubiertos: " + cubiertos + "/" + necesarios + ")");
                	pararCiclo = true;
                }
            }

            if (cubiertos < necesarios) {
                System.out.println(" ! No se pudo cubrir completamente " + rol + 
                                   " (faltaron " + (necesarios - cubiertos) + ")");
            }
        }

        Map<TipoRol, Integer> remanente = cancion.verRolesFaltantes();
        if (remanente.isEmpty()) {
        	logger.info("Se ocuparon todos los roles del recital");
            System.out.println("Resultado: ¡Roles cubiertos!");
        } else {
        	logger.info("Faltan roles por ocupar del recital");
            System.out.println("Resultado: aún faltan roles:");
            remanente.forEach((r, n) -> System.out.println(" - " + r + ": " + n));
        }
    }
    
    private void contratarArtista(Cancion cancion, Artista artista, TipoRol rol) {
    	generarContratacion(artista, cancion, rol);
    }
    
    public void contratarArtistasRecital() {
    	for(Cancion c : Recital.getInstance().getCanciones()) {
    		contratarArtistas(c);
    	}
    }
}