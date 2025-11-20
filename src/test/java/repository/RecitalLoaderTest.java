package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Recital;

import java.nio.file.Files;
import java.nio.file.Path;

public class RecitalLoaderTest {

    public static void cargarRecital() {
        try {
            String json = Files.readString(Path.of("Data/recital_v2.json"));
            Recital recital = new ObjectMapper().readValue(json, Recital.class);
            Recital.setInstance(recital);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}