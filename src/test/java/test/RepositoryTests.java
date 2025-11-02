package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import domain.*;
import repository.*;
import java.util.*;

class RepositoryTests {

    @Test
    void testCargarDatosDesdeJSON() {
        SistemaInicializador init = new SistemaInicializador();
        init.inicializar(
            "Data/artistas.json",
            "Data/recital.json",
            "Data/artistas-discografica.json"
        );

        ArtistaRepository artistaRepo = init.getArtistaRepository();
        assertTrue(artistaRepo.cantidad() > 0);

        CancionRepository cancionRepo = init.getCancionRepository();
        assertTrue(cancionRepo.cantidad() > 0);

        System.out.println("✓ Test básico pasado: Datos cargados correctamente");
    }

    @Test
    void testBuscarArtistaPorNombre() {
        Set<TipoRol> roles = new HashSet<>();
        roles.add(TipoRol.GUITARRA_ELECTRICA);

        Set<String> bandas = new HashSet<>();
        bandas.add("Queen");

        Artista brian = new ArtistaBase("Brian May", roles, bandas);

        ArtistaRepository repo = new ArtistaRepository();
        repo.agregar(brian);

        Artista encontrado = repo.buscarPorNombre("Brian May");

        assertNotNull(encontrado);
        assertEquals("Brian May", encontrado.getNombre());

        System.out.println("✓ Test básico pasado: Búsqueda por nombre funciona");
    }

    @Test
    void testAgregarCancion() {
        Map<TipoRol, Integer> roles = new EnumMap<>(TipoRol.class);
        roles.put(TipoRol.VOZ_PRINCIPAL, 1);
        roles.put(TipoRol.GUITARRA_ELECTRICA, 1);

        Cancion cancion = new Cancion("We Will Rock You", roles);

        CancionRepository repo = new CancionRepository();
        repo.agregar(cancion);

        assertEquals(1, repo.cantidad());
        assertNotNull(repo.buscarPorTitulo("We Will Rock You"));

        System.out.println("✓ Test básico pasado: Agregar canción funciona");
    }
}
