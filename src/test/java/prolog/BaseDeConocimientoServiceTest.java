package prolog;

import domain.*;
import org.junit.jupiter.api.Test;
import repository.ArtistaRepository;
import repository.CancionRepository;
import services.BaseDeConocimientoService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BaseDeConocimientoServiceTest {

    @Test
    void generaHechos_usandoRepos_sinArchivos() {
        ArtistaRepository artRepo = new ArtistaRepository();
        CancionRepository canRepo = new CancionRepository();

        Set<TipoRol> rolesMay = new HashSet<>(Arrays.asList(
            TipoRol.GUITARRA_ELECTRICA, TipoRol.VOZ_SECUNDARIA));
        Set<String> bandasMay = new HashSet<>(Collections.singletonList("Queen"));
        Artista brian = new ArtistaBase("Brian May", rolesMay, bandasMay);
        artRepo.agregar(brian);

        Set<TipoRol> rolesBowie = new HashSet<>(Collections.singletonList(TipoRol.VOZ_PRINCIPAL));
        Set<String> bandasBowie = new HashSet<>(Collections.singletonList("David Bowie"));
        Artista bowie = new ArtistaExterno("David Bowie", rolesBowie, bandasBowie, 1500, 2);
        artRepo.agregar(bowie);

        Map<TipoRol,Integer> req = new EnumMap<>(TipoRol.class);
        req.put(TipoRol.VOZ_PRINCIPAL, 2);
        req.put(TipoRol.GUITARRA_ELECTRICA, 1);
        req.put(TipoRol.BAJO, 1);
        req.put(TipoRol.BATERIA, 1);
        canRepo.agregar(new Cancion("Under Pressure", req));

        String hechos = new BaseDeConocimientoService()
            .generarHechos(artRepo, canRepo, Set.of("Brian May"));

        assertAll(
            () -> assertTrue(hechos.contains("artista_base(brian_may).")),
            () -> assertTrue(hechos.contains("artista_externo(david_bowie).")),
            () -> assertTrue(hechos.contains("tiene_rol(brian_may, guitarra_electrica).")),
            () -> assertTrue(hechos.contains("banda(brian_may, queen).")),
            () -> assertTrue(hechos.contains("cancion(under_pressure).")),
            () -> assertTrue(hechos.contains("requiere(under_pressure, voz_principal, 2).")),
            () -> assertTrue(hechos.contains("requiere(under_pressure, guitarra_electrica, 1)."))
        );
    }
}
