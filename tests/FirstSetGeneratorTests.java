package tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import grammar_objects.*;
import helperObjects.FirstSetGenerator;

public class FirstSetGeneratorTests {
    
    @Test
    public void normalUsage() {
        Set<ProductionRule> productionRules = new HashSet<>();
        productionRules.add(new ProductionRule(
            new NonTerminal("S"), 
            new LexicalElement[] {
                new NonTerminal("T"),
                new Token("c")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("S"), 
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("b")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("T"), 
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("d")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("E"), 
            new LexicalElement[] {
                new NonTerminal("S"),
                new Token("a")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("E"), 
            new LexicalElement[] {
                new Token("")
            }
        ));

        Set<NonTerminal> nonTerminals = new HashSet<>();
        nonTerminals.add(new NonTerminal("S"));
        nonTerminals.add(new NonTerminal("T"));
        nonTerminals.add(new NonTerminal("E"));


        HashMap<NonTerminal, Set<Token>> actualSets = FirstSetGenerator.generate(productionRules, nonTerminals);


        HashMap<NonTerminal, Set<Token>> expectedSets = new HashMap<>();
        expectedSets.put(
            new NonTerminal("S"),
            Set.of(
                new Token("b"),
                new Token("d")
        ));
        expectedSets.put(
            new NonTerminal("T"),
            Set.of(
                new Token("b"),
                new Token("d")
        ));
        expectedSets.put(
            new NonTerminal("E"),
            Set.of(
                new Token(""),
                new Token("b"),
                new Token("d")
        ));

        assertEquals(expectedSets, actualSets);
    }

    @Test
    public void largerGrammar() {
        Set<ProductionRule> productionRules = new HashSet<>();
        productionRules.add(new ProductionRule(
            new NonTerminal("e"), 
            new LexicalElement[] {
                new NonTerminal("t"),
                new NonTerminal("e'")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("e'"), 
            new LexicalElement[] {
                new Token("+"),
                new NonTerminal("t"),
                new NonTerminal("e'")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("e'"), 
            new LexicalElement[] {
                new Token("")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("t"), 
            new LexicalElement[] {
                new NonTerminal("f"),
                new NonTerminal("t'")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("t'"), 
            new LexicalElement[] {
                new Token("*"),
                new NonTerminal("f"),
                new NonTerminal("t'")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("t'"), 
            new LexicalElement[] {
                new Token("")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("f"), 
            new LexicalElement[] {
                new Token("("),
                new NonTerminal("e"),
                new Token(")")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("f"), 
            new LexicalElement[] {
                new Token("x")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("f"), 
            new LexicalElement[] {
                new Token("y")
            }
        ));

        Set<NonTerminal> nonTerminals = new HashSet<>();
        nonTerminals.add(new NonTerminal("e"));
        nonTerminals.add(new NonTerminal("e'"));
        nonTerminals.add(new NonTerminal("t"));
        nonTerminals.add(new NonTerminal("t'"));
        nonTerminals.add(new NonTerminal("f"));


        HashMap<NonTerminal, Set<Token>> actualSets = FirstSetGenerator.generate(productionRules, nonTerminals);


        HashMap<NonTerminal, Set<Token>> expectedSets = new HashMap<>();
        expectedSets.put(
            new NonTerminal("e"),
            Set.of(
                new Token("y"),
                new Token("x"),
                new Token("(")
        ));
        expectedSets.put(
            new NonTerminal("e'"),
            Set.of(
                new Token("+"),
                new Token("")
        ));
        expectedSets.put(
            new NonTerminal("t"),
            Set.of(
                new Token("y"),
                new Token("x"),
                new Token("(")
        ));
        expectedSets.put(
            new NonTerminal("t'"),
            Set.of(
                new Token("*"),
                new Token("")
        ));
         expectedSets.put(
            new NonTerminal("f"),
            Set.of(
                new Token("y"),
                new Token("x"),
                new Token("(")
        ));

        assertEquals(expectedSets, actualSets);
    }

    //TODO: add tests for various empty token positions
}
