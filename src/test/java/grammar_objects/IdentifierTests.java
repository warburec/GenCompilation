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
    public void similarTokenEquality_grammatically() {
        Identifier ident = new Identifier("Identifier", "x");
        Token token = new Token("Identifier");

        assertTrue(ident.equals(token));
        assertTrue(ident.grammaticallyEquals(token));
    }

    @Test
    public void similarTokenEquality_exact() {
        Identifier ident = new Identifier("Identifier", "x");
        Token token = new Token("Identifier");

        assertFalse(ident.exactlyEquals(token));
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
