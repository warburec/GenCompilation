package grammar_objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProductionRuleTest {
    
    @Test
    public void equality() {
        ProductionRule original = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
        });

        ProductionRule identical = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
        });
        ProductionRule nullNonTerminal = new ProductionRule(
            null,
            new LexicalElement[] {
                new Token("b")
        });
        ProductionRule nullProductionSequence = new ProductionRule(
            new NonTerminal("A"),
            null
        );
        ProductionRule incorrectNonTerminal = new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("b")
        });
        ProductionRule incorrectProductionSequence = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a")
        });
        ProductionRule incorrectProductionSequence_usingNonTerminal = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new NonTerminal("b")
        });

        assertEquals(original, identical);
        assertNotEquals(original, nullNonTerminal);
        assertNotEquals(original, nullProductionSequence);
        assertNotEquals(original, incorrectNonTerminal);
        assertNotEquals(original, incorrectProductionSequence);
        assertNotEquals(original, incorrectProductionSequence_usingNonTerminal);
    }

    @Test
    public void reversedEquality() {
        ProductionRule original = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
        });

        ProductionRule identical = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
        });
        ProductionRule nullNonTerminal = new ProductionRule(
            null,
            new LexicalElement[] {
                new Token("b")
        });
        ProductionRule nullProductionSequence = new ProductionRule(
            new NonTerminal("A"),
            null
        );
        ProductionRule incorrectNonTerminal = new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("b")
        });
        ProductionRule incorrectProductionSequence = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a")
        });
        ProductionRule incorrectProductionSequence_usingNonTerminal = new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new NonTerminal("b")
        });

        assertEquals(identical, original);
        assertNotEquals(nullNonTerminal, original);
        assertNotEquals(nullProductionSequence, original);
        assertNotEquals(incorrectNonTerminal, original);
        assertNotEquals(incorrectProductionSequence, original);
        assertNotEquals(incorrectProductionSequence_usingNonTerminal, original);
    }

}
