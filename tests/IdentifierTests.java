package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import grammar_objects.*;
import grammar_objects.Identifier.ConflictingTypesException;

public class IdentifierTests {
    
    @Test
    public void identifierEquality() {
        Identifier i1 = new Identifier("Identifier", "String", "x");
        Identifier i2 = new Identifier("Identifier", "String", "x");

        assertEquals(i1, i2);
    }

    @Test
    public void similarTokenEquality() {
        Identifier ident = new Identifier("Identifier", "String", "x");
        Token token = new Token("Identifier");

        assertNotEquals(ident, token);
    }

    @Test
    public void differentIdentifierNamesGrammatically() {
        Identifier i1 = new Identifier("Identifier", "String", "x");
        Identifier i2 = new Identifier("Identifier", "String", "y");

        assertTrue(i1.equals(i2));
        assertTrue(i1.grammaticallyEquals(i2));
    }

    @Test
    public void differentIdentifierNamesExact() {
        Identifier i1 = new Identifier("Identifier", "String", "x");
        Identifier i2 = new Identifier("Identifier", "String", "y");

        assertFalse(i1.exactlyEquals(i2));
    }

    @Test
    public void differingTypes() {
        Identifier i1 = new Identifier("Identifier", "String", "x");
        Identifier i2 = new Identifier("Identifier", "Integer", "x");

        assertThrows(ConflictingTypesException.class, () -> i1.exactlyEquals(i2));
    }
}
