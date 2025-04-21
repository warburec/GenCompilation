package component_construction.builders.grammar_objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import grammar_objects.*;
import component_construction.builders.grammar_objects.GrammarBuilder.MissingSentinalException;

public class GrammarBuilderTests {
    
    @Test
    public void basicTwoRuleGrammar() {
        GrammarBuilder builder = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new NonTerminal("B"),
                new Token("a")
        }))
        .addRule(new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("b")
        }));


        Grammar actual = builder.produceGrammar();


        Grammar expected = new Grammar(
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
                        new NonTerminal("B"),
                        new Token("a")
                }),
                new ProductionRule(
                    new NonTerminal("B"),
                    new LexicalElement[] {
                        new Token("b")
                })
            ), 
            new NonTerminal("A")
        );

        assertEquals(expected, actual);
    }

    @Test
    public void missingSentinal() {
        GrammarBuilder builder = new GrammarBuilder()
        .addRule(new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new NonTerminal("B"),
                new Token("a")
        }))
        .addRule(new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("b")
        }));


        assertThrows(MissingSentinalException.class, () -> builder.produceGrammar());
    }
}
