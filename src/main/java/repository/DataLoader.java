package repository;

import domain.Artista;
import domain.Cancion;
import domain.TipoDeArtista;
import domain.TipoRol;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DataLoader {

    public List<Artista> cargarArtistas(String path) {
        String json = readFile(path);
        List<String> objs = splitTopLevelObjects(json);
        List<Artista> out = new ArrayList<>();

        for (String o : objs) {
            Map<String, Object> m = parseObject(o);

            String nombre = (String) m.get("nombre");

            @SuppressWarnings("unchecked")
            List<Object> rolesRaw = (List<Object>) m.get("roles");
            Set<TipoRol> roles = rolesRaw == null ? Set.of()
                    : rolesRaw.stream()
                    .map(x -> TipoRol.fromTexto((String) x))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            @SuppressWarnings("unchecked")
            List<Object> bandasRaw = (List<Object>) m.get("bandas");
            Set<String> bandas = bandasRaw == null ? Set.of()
                    : bandasRaw.stream().map(x -> (String) x)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            Object costoObj = m.get("costo");
            double costo = (costoObj instanceof Number) ? ((Number) costoObj).doubleValue() : 0.0;

            TipoDeArtista tipo = (costo == 0.0) ? TipoDeArtista.BASE : TipoDeArtista.EXTERNO;
            Artista artista = new Artista(nombre, tipo, roles, bandas, costo);

            out.add(artista);
        }
        return out;
    }

    public List<Cancion> cargarCanciones(String path) {
        String json = readFile(path);
        List<String> objs = splitTopLevelObjects(json);
        List<Cancion> out = new ArrayList<>();

        for (String o : objs) {
            Map<String, Object> m = parseObject(o);

            String titulo = (String) m.get("titulo");

            @SuppressWarnings("unchecked")
            List<Object> reqRaw = (List<Object>) m.get("rolesRequeridos");
            Map<TipoRol, Integer> rolesRequeridos = new EnumMap<>(TipoRol.class);

            if (reqRaw != null) {
                for (Object x : reqRaw) {
                    TipoRol rol = TipoRol.fromTexto((String) x);
                    rolesRequeridos.merge(rol, 1, Integer::sum);
                }
            }

            Cancion cancion = new Cancion(titulo, rolesRequeridos);
            out.add(cancion);
        }
        return out;
    }

    public List<String> cargarNombresArtistasBase(String path) {
        String json = readFile(path);
        List<Object> arr = parseTopLevelArray(json);
        List<String> out = new ArrayList<>(arr.size());

        for (Object o : arr) {
            if (o instanceof String) {
                out.add((String) o);
            } else {
                throw new IllegalArgumentException("Se esperaba string en arreglo: " + o);
            }
        }
        return out;
    }

    private static String readFile(String path) {
        try {
            return Files.readString(Path.of(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo leer: " + path, e);
        }
    }

    private static List<String> splitTopLevelObjects(String json) {
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]"))
            throw new IllegalArgumentException("Se esperaba un arreglo JSON");
        String body = json.substring(1, json.length() - 1).trim();
        List<String> items = new ArrayList<>();
        int level = 0, start = 0; boolean inStr = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '"' && (i == 0 || body.charAt(i - 1) != '\\')) inStr = !inStr;
            if (inStr) continue;
            if (c == '{') level++;
            else if (c == '}') level--;
            else if (c == ',' && level == 0) {
                String chunk = body.substring(start, i).trim();
                if (!chunk.isEmpty()) items.add(chunk);
                start = i + 1;
            }
        }
        String last = body.substring(start).trim();
        if (!last.isEmpty()) items.add(last);
        return items;
    }

    private static Map<String, Object> parseObject(String obj) {
        obj = obj.trim();
        if (!obj.startsWith("{") || !obj.endsWith("}"))
            throw new IllegalArgumentException("Se esperaba un objeto JSON");
        String body = obj.substring(1, obj.length() - 1).trim();
        Map<String, Object> map = new LinkedHashMap<>();
        int start = 0; boolean inStr = false; int level = 0;
        List<String> pairs = new ArrayList<>();
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '"' && (i == 0 || body.charAt(i - 1) != '\\')) inStr = !inStr;
            if (inStr) continue;
            if (c == '[' || c == '{') level++;
            else if (c == ']' || c == '}') level--;
            else if (c == ',' && level == 0) {
                pairs.add(body.substring(start, i).trim());
                start = i + 1;
            }
        }
        if (!body.isEmpty()) pairs.add(body.substring(start).trim());

        for (String p : pairs) {
            int colon = indexOfColon(p);
            String key = unquote(p.substring(0, colon).trim());
            String val = p.substring(colon + 1).trim();
            map.put(key, parseValue(val));
        }
        return map;
    }

    private static int indexOfColon(String s) {
        boolean inStr = false; int level = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) inStr = !inStr;
            if (inStr) continue;
            if (c == '[' || c == '{') level++;
            else if (c == ']' || c == '}') level--;
            else if (c == ':' && level == 0) return i;
        }
        return -1;
    }

    private static Object parseValue(String v) {
        if (v.startsWith("\"")) return unquote(v);
        if (v.startsWith("{")) return parseObject(v);
        if (v.startsWith("[")) return parseArray(v);
        if (v.matches("-?\\d+(\\.\\d+)?")) return v.contains(".") ? Double.parseDouble(v) : Integer.parseInt(v);
        if ("true".equals(v) || "false".equals(v)) return Boolean.parseBoolean(v);
        if ("null".equals(v)) return null;
        throw new IllegalArgumentException("Valor JSON no soportado: " + v);
    }

    private static List<Object> parseArray(String a) {
        a = a.trim();
        String body = a.substring(1, a.length() - 1).trim();
        List<Object> res = new ArrayList<>();
        if (body.isEmpty()) return res;
        int start = 0; boolean inStr = false; int level = 0;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '"' && (i == 0 || body.charAt(i - 1) != '\\')) inStr = !inStr;
            if (inStr) continue;
            if (c == '[' || c == '{') level++;
            else if (c == ']' || c == '}') level--;
            else if (c == ',' && level == 0) {
                res.add(parseValue(body.substring(start, i).trim()));
                start = i + 1;
            }
        }
        res.add(parseValue(body.substring(start).trim()));
        return res;
    }

    private static List<Object> parseTopLevelArray(String json) {
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]"))
            throw new IllegalArgumentException("Se esperaba un arreglo JSON");
        return parseArray(json);
    }

    private static String unquote(String s) {
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
        }
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
