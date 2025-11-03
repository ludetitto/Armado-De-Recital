package services;

import domain.Artista;
import domain.ArtistaExterno;
import domain.Cancion;
import domain.TipoRol;
import repository.ArtistaRepository;
import repository.CancionRepository;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class BaseDeConocimientoService {

    public String generarHechos(ArtistaRepository artistaRepo,
                                CancionRepository cancionRepo,
                                Set<String> nombresBaseOpcional) {
        StringBuilder sb = new StringBuilder();

        for (Artista a : artistaRepo.todos()) {
            String aId = atom(a.getNombre());
            boolean esBase = a.esBase() || (nombresBaseOpcional != null && nombresBaseOpcional.contains(a.getNombre()));

            if (esBase) {
                sb.append("artista_base(").append(aId).append(").").append('\n');
            } else if (a instanceof ArtistaExterno) {
                // Ajustado a la firma de tu compañero: artista_externo/1
                sb.append("artista_externo(").append(aId).append(").").append('\n');
            }

            for (TipoRol rol : a.getRoles().keySet()) {
                sb.append("tiene_rol(")
                  .append(aId).append(", ")
                  .append(rolAtom(rol))
                  .append(").").append('\n');
            }
            for (String b : a.getBandas()) {
                sb.append("banda(")
                  .append(aId).append(", ")
                  .append(atom(b))
                  .append(").").append('\n');
            }
        }

        for (Cancion c : cancionRepo.todas()) {
            String cId = atom(c.getTitulo());
            sb.append("cancion(").append(cId).append(").").append('\n');
            for (Map.Entry<TipoRol,Integer> e : c.getRolesRequeridos().entrySet()) {
                sb.append("requiere(")
                  .append(cId).append(", ")
                  .append(rolAtom(e.getKey())).append(", ")
                  .append(e.getValue())
                  .append(").").append('\n');
            }
        }

        return sb.toString();
    }

    private static String rolAtom(TipoRol rol) {
        return atom(rol.name().toLowerCase(Locale.ROOT).replace('_', ' '));
    }

    private static String atom(String s) {
        String noAccents = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String lowered = noAccents.toLowerCase(Locale.ROOT);
        String clean = lowered.replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
        if (clean.isEmpty()) clean = "x";
        if (!Character.isLetter(clean.charAt(0))) clean = "x_" + clean;
        return clean;
    }
}
