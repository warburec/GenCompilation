package grammar_objects;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class IdentifierTests {
    
    @Test
    public void identifierEquality() {
        Identifier i1 = new Identifier("Identifier", "x");
        Identifier i2 = new Identifier("Identifier", "x");

        assertEquals(i1, i2);
    }

    @Test
    public void similarTokenEquality() {
        Identifier ident = new Identifier("Identifier", "x");
        Token token = new Token("Identifier");

        assertNotEquals(ident, token);
    }

    @Test
    public void differentIdentifierNamesGrammatically() {
        Identifier i1 = new Identifier("Identifier", "x");
        Identifier i2 = new Identifier("Identifier", "y");

        assertTrue(i1.equals(i2));
        assertTrue(i1.grammaticallyEquals(i2));
    }

    @Test
    public void differentIdentifierNamesExact() {
        Identifier i1 = new Identifier("Identifier", "x");
        Identifier i2 = new Identifier("Identifier","y");

        assertFalse(i1.exactlyEquals(i2));
    }
}
