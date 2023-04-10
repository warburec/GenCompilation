package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import syntaxAnalyser.SyntaxAnalyser;
import tests.testAids.*;
import tests.testAids.GrammarGenerator.Grammar;

public class LR0Parser {
    
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
