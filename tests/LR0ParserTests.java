package tests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import GrammarObjects.*;
import syntaxAnalyser.*;
import tests.testAids.*;
import tests.testAids.GrammarGenerator.Grammar;

public class LR0ParserTests {
    
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
                            })//TODO: More
        };

        LR0Parser syntaxAnalyser = new LR0Parser(tokens,
                                                    nonTerminals,
                                                    productionRules,
                                                    sentinal);

        State[] generatedStates = syntaxAnalyser.getStates();

        State[] expectedStates = new State[] {
            //TODO: Create the 8 expected states
        };
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
