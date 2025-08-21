import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IBANCheckerTest {

    @Test
    void validIban() {
        assertTrue(IBANChecker.validate("DE22790200760027913168")); // Should be true
    }

    @Test
    void invalidIban_wrongCheckDigits() {
        assertFalse(IBANChecker.validate("DE21790200760027913173")); // Should be flase
    }

    @Test
    void invalidIban_wrongLength() {
        assertFalse(IBANChecker.validate("DE227902007600279131")); // Should be false
    }

    @Test
    void invalidIban_unknownCountry() {
        assertFalse(IBANChecker.validate("XX22790200760027913168")); // Should be false
    }
}
