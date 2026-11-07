package grammar_objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TokenTests {
    
    @Test
    public void equality() {
        Token token1 = new Token("A");
        Token token2 = new Token("A");

        assertEquals(token1, token2);
        assertEquals(token2, token1);
    }

    @Test
    public void nonEqualityWithNonTerminals() {
        Token token = new Token("A");
        NonTerminal nonTerminal = new NonTerminal("A");

        assertNotEquals(token, nonTerminal);
        assertNotEquals(nonTerminal, token);
    }

}
