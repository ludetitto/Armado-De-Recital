package domain;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;

public enum TipoRol {
    VOZ_PRINCIPAL, 
    VOZ_SECUNDARIA,
    GUITARRA_ELECTRICA, 
    BAJO, 
    BATERIA,
    PIANO, 
    TECLADOS, 
    COROS;

    private static final Map<String, TipoRol> MAP = Map.of(
        "voz principal", VOZ_PRINCIPAL,
        "voz secundaria", VOZ_SECUNDARIA,
        "guitarra electrica", GUITARRA_ELECTRICA,
        "bajo", BAJO,
        "bateria", BATERIA,
        "piano", PIANO,
        "teclados", TECLADOS,
        "coros", COROS
    );

    public static TipoRol fromTexto(String raw) {
        String key = normalizar(raw);
        TipoRol r = MAP.get(key);
        if (r == null) throw new IllegalArgumentException("Rol desconocido: " + raw);
        return r;
    }

    private static String normalizar(String s) {
        String t = Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return t.toLowerCase(Locale.ROOT).trim();
    }
}
