package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import repository.ArtistaRepository;
import repository.CancionRepository;

public class PrologService {

    private final BaseDeConocimientoService baseDeConocimientoService = new BaseDeConocimientoService();
    private ArtistaRepository artistaRepository;
    private CancionRepository cancionRepository;
    
    public PrologService(ArtistaRepository artistaRepository, CancionRepository cancionRepository) {
    	this.artistaRepository = artistaRepository;
    	this.cancionRepository = cancionRepository;
	}

	public int entrenamientosMinimos(Set<String> nombresBaseOpcional)
            throws IOException, InterruptedException, URISyntaxException {

        String hechos, reglas, programa, consulta, objetivo, out,
        header = ":- discontiguous artista_base/1, artista_externo/1, tiene_rol/2, banda/2, cancion/1, requiere/3.\n";

        Path tmp;
        int resultado;
        Process p;
        
        // 1) Hechos frescos desde repos
        hechos = baseDeConocimientoService.generarHechos(
                artistaRepository, cancionRepository, nombresBaseOpcional);

        // 2) Leer reglas desde el .pl en resources (classpath)
        try (InputStream is = Objects.requireNonNull(
                 getClass().getResourceAsStream("/prolog/reglas_entrenamientos.pl"),
                 "No se encontró /prolog/reglas_entrenamientos.pl en el classpath");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            reglas = br.lines().collect(Collectors.joining("\n"));
        }

        // 3) Unir HECHOS + REGLAS y escribir a un .pl temporal
        programa = header + hechos + "\n\n" + reglas;
        
        tmp = Files.createTempFile("recital_kb_", ".pl");
        Files.writeString(tmp, programa, StandardCharsets.UTF_8);

        // 4) Ejecutar swipl
        
        // objetivo: consulta(File), entrenamientos_minimos(T), imprimir T y terminar
        consulta = "consult('" + escapeForSwipl(tmp.toAbsolutePath().toString()) + "')";
        objetivo = consulta + ",entrenamientos_minimos(T),format('~w~n',[T])";

        // ⚠️ NO redirijas stderr a stdout
        p = new ProcessBuilder("swipl", "-q", "-g", objetivo, "-t", "halt").start();

        // Leer stdout
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            out = br.lines().collect(java.util.stream.Collectors.joining("\n")).trim();
        }

        // Leer stderr (por si querés loguear los warnings/errores)
        String err;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8))) {
            err = br.lines().collect(java.util.stream.Collectors.joining("\n")).trim();
        }

        int code = p.waitFor();

        // Para diagnosticar
        if (code != 0) {
            throw new IllegalStateException("SWI-Prolog terminó con código " + code + ". STDERR:\n" + err);
        }

        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("(\\d+)\\s*$", java.util.regex.Pattern.MULTILINE)
                .matcher(out);

        if (!m.find()) {
            throw new IllegalStateException("No se encontró un entero en la salida de Prolog.\nSTDOUT:\n" + out + "\nSTDERR:\n" + err);
        }

        resultado = Integer.parseInt(m.group(1));
        return resultado;
    }

    // Escapar rutas para SWI
    private static String escapeForSwipl(String path) {
        return path.replace("\\", "\\\\").replace("'", "\\'");
    }
}
