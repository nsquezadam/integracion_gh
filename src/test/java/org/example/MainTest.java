package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void sumar_debeSumarDosEnteros() {
        Main main = new Main();
        assertEquals(5, main.sumar(2, 3));
    }

    @Test
    void main_imprimeMensaje() {
        // Salida en consola
        java.io.PrintStream original = System.out;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(baos));
        try {
            Main.main(new String[]{});
        } finally {
            System.setOut(original);
        }
        String salida = baos.toString();
        assertTrue(salida.contains("Hola Jenkins desde IntelliJ!"));
    }
}
