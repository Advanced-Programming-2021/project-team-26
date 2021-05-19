package view;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ScanTest {
    Scan scan = Scan.getInstance();

    @Test
    void parseInputWithNoDash() {
        String input = "this input has no dash";
        HashMap<String, String> result = scan.parseInput(input);
        assertEquals(0, result.size());
    }

    @Test
    void parseNormalInput() {
        String input = "--this is -normal input";
        HashMap<String, String> result = scan.parseInput(input);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("this"));
        assertTrue(result.containsKey("normal"));

        assertEquals("is", result.get("this"));
        assertEquals("input", result.get("normal"));
    }

    @Test
    void parseKeyWithoutValue() {
        String input = "this -key --has value";
        HashMap<String, String> result = scan.parseInput(input);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("key"));
        assertTrue(result.containsKey("has"));

        assertNull(result.get("key"));
        assertEquals("value", result.get("has"));
    }

    @Test
    void parseInputWithSpace() {
        String input = "this --input contains space -so what";
        HashMap<String, String> result = scan.parseInput(input);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("input"));
        assertTrue(result.containsKey("so"));

        assertEquals("contains space", result.get("input"));
        assertEquals("what", result.get("so"));
    }
}