package grammar_objects;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

public class NonTerminalTests {
    
    @Test
    public void equality() {
        NonTerminal nt1 = new NonTerminal("A");
        NonTerminal nt2 = new NonTerminal("A");

        assertEquals(nt1, nt2);
    }

    @Test
    public void nonEquality() {
        NonTerminal nt1 = new NonTerminal("A");
        NonTerminal nt2 = new NonTerminal("B");

        assertNotEquals(nt1, nt2);
    }

    @Test
    public void setContains() {
        NonTerminal nt1 = new NonTerminal("A");
        NonTerminal nt2 = new NonTerminal("A");
        Set<NonTerminal> set = new HashSet<>();
        set.add(nt1);

        assertTrue(set.contains(nt2));
    }
}
