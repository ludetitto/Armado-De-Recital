package repository;

import domain.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DataLoader {
    
    // ====== API pública ======
    
    /**
     * Carga artistas desde un archivo JSON
     * @param path Ruta al archivo artistas.json
     * @return Lista de artistas (Base o Externos)
     */
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
            
            Object maxObj = m.get("maxCanciones");
            int maxCanciones = (maxObj instanceof Number) ? ((Number) maxObj).intValue() : 0;
            
            // Decidir si es Base o Externo según el costo
            Artista artista;
            if (costo == 0.0) {
                artista = new ArtistaBase(nombre, roles, bandas);
            } else {
                artista = new ArtistaExterno(nombre, roles, bandas, costo, maxCanciones);
            }
            
            out.add(artista);
        }
        return out;
    }
    
    /**
     * Carga canciones desde un archivo JSON
     * @param path Ruta al archivo recital.json
     * @return Lista de canciones
     */
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
    
    /**
     * Carga nombres de artistas base desde un archivo JSON
     * @param path Ruta al archivo artistas-discografica.json
     * @return Lista de nombres de artistas base
     */
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
    
    // ====== Helpers mínimos (sin libs externas) ======
    
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