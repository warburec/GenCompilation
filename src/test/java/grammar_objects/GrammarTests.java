package grammar_objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.*;

public class GrammarTests {
    
    @Test
    public void fullEquality() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertEquals(grammar1, grammar2);
    }

    @Test
    public void lessTokens() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void moreTokens() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b"),
                new Token("c")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertNotEquals(grammar1, grammar2);
    }
    
    @Test
    public void lessNonTerminals() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void moreNonTerminals() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B"),
                new NonTerminal("C")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void lessRules() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void moreRules() {
        Grammar grammar1 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                )
            ),
            new NonTerminal("A")
        );

        Grammar grammar2 = new Grammar(
            Set.of(
                new Token("a"),
                new Token("b")
            ),
            Set.of(
                new NonTerminal("A"),
                new NonTerminal("B")
            ),
            List.of(
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ),
                new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                )
            ),
            new NonTerminal("A")
        );

        assertNotEquals(grammar1, grammar2);
    }

    //TODO: Test with null or empty values
}
