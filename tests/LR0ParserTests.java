package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import GrammarObjects.*;
import syntaxAnalyser.*;
import tests.testAids.*;
import tests.testAids.GrammarGenerator.Grammar;

public class LR0ParserTests {
    
    @Test
    public void nonTerminalStart() {
        Token[] tokens = new Token[] {};
        NonTerminal sentinal = new NonTerminal("Start");
        NonTerminal[] nonTerminals = new NonTerminal[] {
            new NonTerminal("Start")
        };
        ProductionRule[] productionRules = new ProductionRule[] {};

        assertThrows(RuntimeException.class, () -> new LR0Parser(tokens,
                                                            nonTerminals,
                                                            productionRules,
                                                            sentinal));
    }

    @Test
    public void smallTestGrammar() {
        Token[] tokens = new Token[] {
            new Token("0"),
            new Token("1"),
            new Token("*"),
            new Token("+")
        };
        NonTerminal sentinal = new NonTerminal("E");
        NonTerminal[] nonTerminals = new NonTerminal[] {
            new NonTerminal("E"),
            new NonTerminal("B")
        };
        ProductionRule[] productionRules = new ProductionRule[] {
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("E"),
                                new Token("+"),
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("E"),
                                new Token("*"),
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("B"),
                            new LexicalElement[] {
                                new Token("0")
                            }),
            new ProductionRule(new NonTerminal("B"),
                            new LexicalElement[] {
                                new Token("1")
                            })
        };


        //Act
        LR0Parser syntaxAnalyser = new LR0Parser(tokens,
                                                    nonTerminals,
                                                    productionRules,
                                                    sentinal);
        Set<State> generatedStates = syntaxAnalyser.getStates();


        ProductionRule extraRootRule = new ProductionRule(null, new LexicalElement[] {sentinal});

        List<State> expectedStates = new ArrayList<>();
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(productionRules[1], 0),
                new GrammarPosition(productionRules[0], 0),
                new GrammarPosition(productionRules[2], 0),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            null
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(productionRules[1], 1),
                new GrammarPosition(productionRules[0], 1),
            }),
            expectedStates.get(0)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 2),
            }),
            expectedStates.get(1)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 3),
            }),
            expectedStates.get(2)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 2),
            }),
            expectedStates.get(1)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 3),
            }),
            expectedStates.get(3)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[2], 1),
            }),
            expectedStates.get(0)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[3], 1),
            }),
            expectedStates.get(0)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[4], 1),
            }),
            expectedStates.get(0)
        ));

        Set<State> expectedStateSet = Set.of((State[]) expectedStates.toArray());
        
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void basicGrammar() {
        GrammarParts parts = GrammarGenerator.generateParts(Grammar.IntegerComputation);
        SyntaxAnalyser syntaxAnalyser = new syntaxAnalyser.LR0Parser(parts.tokens(),
                                                                    parts.nonTerminals(),
                                                                    parts.productionRules(),
                                                                    parts.sentinal());
        assertTrue(false);
    }
}
