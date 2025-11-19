package repository;

import domain.*;
import java.util.List;

public class SistemaInicializador {
    private ArtistaRepository artistaRepo;
    private CancionRepository cancionRepo;
    private RecitalRepository recitalRepo;
    private DataLoader loader;
    
    public SistemaInicializador() {
        this.artistaRepo = new ArtistaRepository();
        this.cancionRepo = new CancionRepository();
        this.recitalRepo = new RecitalRepository(Recital.getInstance());
        this.loader = new DataLoader();
    }
    

    public void inicializar(String pathArtistas, String pathRecital, String pathArtistasBase) {
        System.out.println("=== Iniciando carga de datos ===");
        
        // 1. Cargar artistas
        System.out.println("Cargando artistas...");
        List<Artista> artistas = loader.cargarArtistas(pathArtistas);
        for (Artista artista : artistas) {
            artistaRepo.agregar(artista);
        }
        System.out.println("✓ " + artistas.size() + " artistas cargados");
        
        // 2. Cargar nombres de artistas base
        System.out.println("Identificando artistas base...");
        List<String> nombresBase = loader.cargarNombresArtistasBase(pathArtistasBase);
        Recital recital = recitalRepo.getRecital();
        
        for (String nombre : nombresBase) {
            Artista artista = artistaRepo.buscarPorNombre(nombre);
            if (artista != null && artista.esBase()) {
                recital.agregarArtistaBase(artista);
            } else {
                System.out.println("⚠ Artista base no encontrado: " + nombre);
            }
        }
        System.out.println("✓ " + nombresBase.size() + " artistas base agregados al recital");
        
        // 3. Cargar canciones
        System.out.println("Cargando canciones...");
        List<Cancion> canciones = loader.cargarCanciones(pathRecital);
        for (Cancion cancion : canciones) {
            cancionRepo.agregar(cancion);
            recital.agregarCancion(cancion);
        }
        System.out.println("✓ " + canciones.size() + " canciones cargadas");
        
        System.out.println("=== Carga completada exitosamente ===\n");
    }
    
    public ArtistaRepository getArtistaRepository() {
        return artistaRepo;
    }
    
    public CancionRepository getCancionRepository() {
        return cancionRepo;
    }
    
    public RecitalRepository getRecitalRepository() {
        return recitalRepo;
    }
}