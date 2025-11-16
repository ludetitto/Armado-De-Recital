package controllers;

import domain.Cancion;
import domain.Recital;
import services.ArtistaService;
import services.ContratacionService;
import services.RecitalService;

import java.util.Set;

public class ContratarArtistasParaRecitalCommand implements ComandoContratacion {

	private ContratacionService contratacionService;

    public ContratarArtistasParaRecitalCommand() {
        this(null, null);
    }

    // Sobrecarga por si luego querés inyectar servicios
    public ContratarArtistasParaRecitalCommand(ArtistaService artistaService, RecitalService recitalService) {
    	contratacionService = new ContratacionService(recitalService, artistaService);
    }

    @Override
    public void ejecutar() {
//        Recital recital = Recital.getInstance();
//        Set<Cancion> canciones = recital.getCanciones();
//
//        if (canciones.isEmpty()) {
//            System.out.println("No hay canciones en el recital.");
//            return;
//        }
//        System.out.println("--- Contratación automática para TODO el recital ---");
//        for (Cancion c : canciones) {
//            var cmd = (artistaServiceOpt != null && recitalServiceOpt != null)
//                    ? new ContratarArtistasParaCancionCommand(artistaServiceOpt, new services.CancionService(), c.getTitulo())
//                    : new ContratarArtistasParaCancionCommand(c.getTitulo());
//            cmd.ejecutar();
//        }
//        var recitalService = (recitalServiceOpt != null) ? recitalServiceOpt : new RecitalService();
//        System.out.println("Resumen roles faltantes globales: " + recitalService.verRolesFaltantes(recital));
//        System.out.println("----------------------------------------------------");
    }

    @Override
    public void deshacer() {
        System.out.println("Deshacer general no implementado para contratación de todo el recital.");
    }
}
