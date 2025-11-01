package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import domain.*;
import repository.*;

import java.util.*;

class RepositoryTests {
    
    private ArtistaRepository artistaRepo;
    private CancionRepository cancionRepo;
    private ContratacionRepository contratacionRepo;
    private RecitalRepository recitalRepo;
    private SistemaInicializador inicializador;
    
    @BeforeEach
    void setUp() {
        // Inicializar repositorios antes de cada test
        artistaRepo = new ArtistaRepository();
        cancionRepo = new CancionRepository();
        contratacionRepo = new ContratacionRepository();
        recitalRepo = RecitalRepository.getInstance();
        inicializador = new SistemaInicializador();
    }
    
    // ==================== TESTS DE ARTISTA REPOSITORY ====================
    
    @Test
    @DisplayName("Test: Agregar y buscar artista por nombre")
    void testAgregarYBuscarArtista() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.GUITARRA_ELECTRICA));
        Set<String> bandas = new HashSet<>(Arrays.asList("Queen"));
        Artista artista = new ArtistaBase("Brian May", roles, bandas);
        
        // Act
        artistaRepo.agregar(artista);
        Artista encontrado = artistaRepo.buscarPorNombre("Brian May");
        
        // Assert
        assertNotNull(encontrado);
        assertEquals("Brian May", encontrado.getNombre());
        assertTrue(encontrado.esBase());
    }
    
    @Test
    @DisplayName("Test: Agregar artista null lanza excepción")
    void testAgregarArtistaNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            artistaRepo.agregar(null);
        });
    }
    
    @Test
    @DisplayName("Test: Buscar artistas por rol")
    void testBuscarArtistasPorRol() {
        // Arrange
        Set<TipoRol> roles1 = new HashSet<>(Arrays.asList(TipoRol.VOZ_PRINCIPAL));
        Set<TipoRol> roles2 = new HashSet<>(Arrays.asList(TipoRol.GUITARRA_ELECTRICA));
        Set<String> bandas = new HashSet<>(Arrays.asList("Test Band"));
        
        Artista artista1 = new ArtistaExterno("Cantante", roles1, bandas, 1000, 5);
        Artista artista2 = new ArtistaBase("Guitarrista", roles2, bandas);
        
        artistaRepo.agregar(artista1);
        artistaRepo.agregar(artista2);
        
        // Act
        List<Artista> vocalistas = artistaRepo.buscarPorRol(TipoRol.VOZ_PRINCIPAL);
        
        // Assert
        assertEquals(1, vocalistas.size());
        assertEquals("Cantante", vocalistas.get(0).getNombre());
    }
    
    @Test
    @DisplayName("Test: Obtener solo artistas base")
    void testObtenerArtistasBase() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.BAJO));
        Set<String> bandas = new HashSet<>(Arrays.asList("Queen"));
        
        Artista base = new ArtistaBase("John Deacon", roles, bandas);
        Artista externo = new ArtistaExterno("George Michael", roles, bandas, 1000, 3);
        
        artistaRepo.agregar(base);
        artistaRepo.agregar(externo);
        
        // Act
        List<Artista> artistasBase = artistaRepo.obtenerArtistasBase();
        
        // Assert
        assertEquals(1, artistasBase.size());
        assertTrue(artistasBase.get(0).esBase());
    }
    
    @Test
    @DisplayName("Test: Obtener solo artistas externos")
    void testObtenerArtistasExternos() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.VOZ_PRINCIPAL));
        Set<String> bandas = new HashSet<>(Arrays.asList("Test"));
        
        Artista base = new ArtistaBase("Base", roles, bandas);
        Artista externo = new ArtistaExterno("Externo", roles, bandas, 1500, 2);
        
        artistaRepo.agregar(base);
        artistaRepo.agregar(externo);
        
        // Act
        List<ArtistaExterno> externos = artistaRepo.obtenerArtistasExternos();
        
        // Assert
        assertEquals(1, externos.size());
        assertFalse(externos.get(0).esBase());
    }
    
    @Test
    @DisplayName("Test: Eliminar artista")
    void testEliminarArtista() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.BATERIA));
        Set<String> bandas = new HashSet<>();
        Artista artista = new ArtistaBase("Baterista", roles, bandas);
        
        artistaRepo.agregar(artista);
        assertEquals(1, artistaRepo.cantidad());
        
        // Act
        artistaRepo.eliminar("Baterista");
        
        // Assert
        assertEquals(0, artistaRepo.cantidad());
        assertNull(artistaRepo.buscarPorNombre("Baterista"));
    }
    
    // ==================== TESTS DE CANCION REPOSITORY ====================
    
    @Test
    @DisplayName("Test: Agregar y buscar canción por título")
    void testAgregarYBuscarCancion() {
        // Arrange
        Map<TipoRol, Integer> roles = new EnumMap<>(TipoRol.class);
        roles.put(TipoRol.VOZ_PRINCIPAL, 1);
        roles.put(TipoRol.GUITARRA_ELECTRICA, 1);
        
        Cancion cancion = new Cancion("We Will Rock You", roles);
        
        // Act
        cancionRepo.agregar(cancion);
        Cancion encontrada = cancionRepo.buscarPorTitulo("We Will Rock You");
        
        // Assert
        assertNotNull(encontrada);
        assertEquals("We Will Rock You", encontrada.getTitulo());
        assertEquals(TipoEstado.BORRADOR, encontrada.getEstado());
    }
    
    @Test
    @DisplayName("Test: Agregar canción null lanza excepción")
    void testAgregarCancionNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cancionRepo.agregar(null);
        });
    }
    
    @Test
    @DisplayName("Test: Obtener canciones por estado")
    void testObtenerCancionesPorEstado() {
        // Arrange
        Map<TipoRol, Integer> roles = new EnumMap<>(TipoRol.class);
        roles.put(TipoRol.VOZ_PRINCIPAL, 1);
        
        Cancion cancion1 = new Cancion("Cancion 1", roles);
        Cancion cancion2 = new Cancion("Cancion 2", roles);
        
        cancionRepo.agregar(cancion1);
        cancionRepo.agregar(cancion2);
        
        // Act
        List<Cancion> borradores = cancionRepo.obtenerPorEstado(TipoEstado.BORRADOR);
        
        // Assert
        assertEquals(2, borradores.size());
    }
    
    @Test
    @DisplayName("Test: Obtener canciones incompletas")
    void testObtenerCancionesIncompletas() {
        // Arrange
        Map<TipoRol, Integer> roles = new EnumMap<>(TipoRol.class);
        roles.put(TipoRol.VOZ_PRINCIPAL, 1);
        Cancion cancion = new Cancion("Test Song", roles);
        
        cancionRepo.agregar(cancion);
        
        // Act
        List<Cancion> incompletas = cancionRepo.obtenerIncompletas();
        
        // Assert
        assertEquals(1, incompletas.size());
    }
    
    // ==================== TESTS DE CONTRATACION REPOSITORY ====================
    
    @Test
    @DisplayName("Test: Agregar y buscar contratación")
    void testAgregarYBuscarContratacion() {
        // Arrange
        Set<TipoRol> rolesArtista = new HashSet<>(Arrays.asList(TipoRol.VOZ_PRINCIPAL));
        Set<String> bandas = new HashSet<>();
        Artista artista = new ArtistaExterno("Cantante", rolesArtista, bandas, 1000, 5);
        
        Map<TipoRol, Integer> rolesCancion = new EnumMap<>(TipoRol.class);
        rolesCancion.put(TipoRol.VOZ_PRINCIPAL, 1);
        Cancion cancion = new Cancion("Test Song", rolesCancion);
        
        Contratacion contratacion = new Contratacion(artista, cancion, TipoRol.VOZ_PRINCIPAL, 1000, 0);
        
        // Act
        contratacionRepo.agregar(contratacion);
        Contratacion encontrada = contratacionRepo.buscar(artista, cancion, TipoRol.VOZ_PRINCIPAL);
        
        // Assert
        assertNotNull(encontrada);
        assertEquals(artista, encontrada.getArtista());
        assertEquals(cancion, encontrada.getCancion());
        assertEquals(1000, encontrada.getCostoFinal());
    }
    
    @Test
    @DisplayName("Test: Obtener contrataciones por artista")
    void testObtenerContratacionesPorArtista() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.GUITARRA_ELECTRICA));
        Set<String> bandas = new HashSet<>();
        Artista artista = new ArtistaExterno("Guitarrista", roles, bandas, 800, 3);
        
        Map<TipoRol, Integer> rolesCancion1 = new EnumMap<>(TipoRol.class);
        rolesCancion1.put(TipoRol.GUITARRA_ELECTRICA, 1);
        Cancion cancion1 = new Cancion("Song 1", rolesCancion1);
        Cancion cancion2 = new Cancion("Song 2", rolesCancion1);
        
        Contratacion c1 = new Contratacion(artista, cancion1, TipoRol.GUITARRA_ELECTRICA, 800, 0);
        Contratacion c2 = new Contratacion(artista, cancion2, TipoRol.GUITARRA_ELECTRICA, 800, 0);
        
        contratacionRepo.agregar(c1);
        contratacionRepo.agregar(c2);
        
        // Act
        List<Contratacion> contrataciones = contratacionRepo.obtenerPorArtista(artista);
        
        // Assert
        assertEquals(2, contrataciones.size());
    }
    
    @Test
    @DisplayName("Test: Calcular costo total de contrataciones")
    void testCalcularCostoTotal() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.PIANO));
        Set<String> bandas = new HashSet<>();
        Artista artista = new ArtistaExterno("Pianista", roles, bandas, 1200, 2);
        
        Map<TipoRol, Integer> rolesCancion = new EnumMap<>(TipoRol.class);
        rolesCancion.put(TipoRol.PIANO, 1);
        Cancion cancion = new Cancion("Piano Song", rolesCancion);
        
        Contratacion c1 = new Contratacion(artista, cancion, TipoRol.PIANO, 1200, 0);
        Contratacion c2 = new Contratacion(artista, cancion, TipoRol.PIANO, 600, 0.5);
        
        contratacionRepo.agregar(c1);
        contratacionRepo.agregar(c2);
        
        // Act
        double costoTotal = contratacionRepo.calcularCostoTotal();
        
        // Assert
        assertEquals(1800, costoTotal, 0.01);
    }
    
    @Test
    @DisplayName("Test: Eliminar contratación")
    void testEliminarContratacion() {
        // Arrange
        Set<TipoRol> roles = new HashSet<>(Arrays.asList(TipoRol.BAJO));
        Set<String> bandas = new HashSet<>();
        Artista artista = new ArtistaExterno("Bajista", roles, bandas, 700, 4);
        
        Map<TipoRol, Integer> rolesCancion = new EnumMap<>(TipoRol.class);
        rolesCancion.put(TipoRol.BAJO, 1);
        Cancion cancion = new Cancion("Bass Song", rolesCancion);
        
        Contratacion contratacion = new Contratacion(artista, cancion, TipoRol.BAJO, 700, 0);
        
        contratacionRepo.agregar(contratacion);
        assertEquals(1, contratacionRepo.cantidad());
        
        // Act
        boolean eliminada = contratacionRepo.eliminar(contratacion);
        
        // Assert
        assertTrue(eliminada);
        assertEquals(0, contratacionRepo.cantidad());
    }
    
    // ==================== TESTS DE CARGA DE DATOS ====================
    
    @Test
    @DisplayName("Test: Cargar artistas desde JSON")
    void testCargarArtistasDesdeJSON() {
        // Act
        inicializador.inicializar(
            "Data/artistas.json",
            "Data/recital.json",
            "Data/artistas-discografica.json"
        );
        
        ArtistaRepository repo = inicializador.getArtistaRepository();
        
        // Assert
        assertTrue(repo.cantidad() > 0);
        assertNotNull(repo.buscarPorNombre("Brian May"));
        assertNotNull(repo.buscarPorNombre("George Michael"));
    }
    
    @Test
    @DisplayName("Test: Cargar canciones desde JSON")
    void testCargarCancionesDesdeJSON() {
        // Act
        inicializador.inicializar(
            "Data/artistas.json",
            "Data/recital.json",
            "Data/artistas-discografica.json"
        );
        
        CancionRepository repo = inicializador.getCancionRepository();
        
        // Assert
        assertTrue(repo.cantidad() > 0);
        assertNotNull(repo.buscarPorTitulo("Somebody to Love"));
        assertNotNull(repo.buscarPorTitulo("We Will Rock You"));
    }
    
    @Test
    @DisplayName("Test: Identificar artistas base correctamente")
    void testIdentificarArtistasBase() {
        // Act
        inicializador.inicializar(
            "Data/artistas.json",
            "Data/recital.json",
            "Data/artistas-discografica.json"
        );
        
        ArtistaRepository repo = inicializador.getArtistaRepository();
        
        // Assert
        Artista brianMay = repo.buscarPorNombre("Brian May");
        assertNotNull(brianMay);
        assertTrue(brianMay.esBase());
        assertEquals(0.0, brianMay.calcularCosto());
    }
    
    @Test
    @DisplayName("Test: Identificar artistas externos correctamente")
    void testIdentificarArtistasExternos() {
        // Act
        inicializador.inicializar(
            "Data/artistas.json",
            "Data/recital.json",
            "Data/artistas-discografica.json"
        );
        
        ArtistaRepository repo = inicializador.getArtistaRepository();
        
        // Assert
        Artista georgeMichael = repo.buscarPorNombre("George Michael");
        assertNotNull(georgeMichael);
        assertFalse(georgeMichael.esBase());
        assertTrue(georgeMichael.calcularCosto() > 0);
    }
    
    @Test
    @DisplayName("Test: RecitalRepository es Singleton")
    void testRecitalRepositorySingleton() {
        // Act
        RecitalRepository instance1 = RecitalRepository.getInstance();
        RecitalRepository instance2 = RecitalRepository.getInstance();
        
        // Assert
        assertSame(instance1, instance2);
    }
    
    @Test
    @DisplayName("Test: Recital contiene artistas base después de inicializar")
    void testRecitalContieneArtistasBase() {
        // Act
        inicializador.inicializar(
            "Data/artistas.json",
            "Data/recital.json",
            "Data/artistas-discografica.json"
        );
        
        Recital recital = inicializador.getRecitalRepository().getRecital();
        
        // Assert
        List<Artista> artistasBase = recital.getArtistasBase();
        assertTrue(artistasBase.size() > 0);
        assertEquals(3, artistasBase.size()); // Brian May, Roger Taylor, John Deacon
    }
}