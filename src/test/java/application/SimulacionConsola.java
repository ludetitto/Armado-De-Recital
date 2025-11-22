package application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;

public abstract class SimulacionConsola {

    private ByteArrayOutputStream out;
    private PrintStream originalOut;

    @BeforeEach
    void setup() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalOut);
    }

    protected String out() {
        return out.toString();
    }
}
