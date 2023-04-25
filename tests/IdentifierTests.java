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
    public void differentIdentifierNames() {
        Identifier i1 = new Identifier("Identifier", "String", "x");
        Identifier i2 = new Identifier("Identifier", "String", "y");

        assertNotEquals(i1, i2);
    }

    @Test
    public void differingTypes() {
        Identifier i1 = new Identifier("Identifier", "String", "x");
        Identifier i2 = new Identifier("Identifier", "Integer", "x");

        assertThrows(ConflictingTypesException.class, () -> i1.equals(i2));
    }
}
