package tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import GrammarObjects.*;
import syntaxAnalyser.*;
import tests.testAids.GrammarParts;
import tests.testAids.GrammarGenerators.TestGrammar;

public class LR0ParserTests {
    
    @Test
    public void nonTerminalNull() {
        Token[] tokens = new Token[] {};
        NonTerminal sentinal = null;
        NonTerminal[] nonTerminals = new NonTerminal[] {
            null
        };
        ProductionRule[] productionRules = new ProductionRule[] {};

        assertThrows(RuntimeException.class, () -> new LR0Parser(tokens,
                                                            nonTerminals,
                                                            productionRules,
                                                            sentinal));
    }

    @Test
    public void smallTestGrammarStates() {
        TestGrammar grammar = new TestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void smallTestGrammarAction() {
        TestGrammar grammar = new TestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Action> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Action> expectedActionTable = grammar.getActionTable();
        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void smallTestGrammarGoto() {
        TestGrammar grammar = new TestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

        Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
        assertEquals(expectedGotoTable, generatedGotoTable);
    }

    @Test
    public void parsingTestGrammarCompleteSentence() {
        TestGrammar grammar = new TestGrammar();
        GrammarParts grammarParts = grammar.getParts();
        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        //1+0*1
        Token[] inputTokens = new Token[] {
            new Token("1"),
            new Token("+"),
            new Token("0"),
            new Token("*"),
            new Token("1")
        };
        
        boolean parseStatus = syntaxAnalyser.analyse(inputTokens);

        assertTrue(parseStatus);
    }

    @Test
    public void parsingTestGrammarIncompleteSentence() {
        TestGrammar grammar = new TestGrammar();
        GrammarParts grammarParts = grammar.getParts();
        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        //1+0*
        Token[] inputTokens = new Token[] {
            new Token("1"),
            new Token("+"),
            new Token("0"),
            new Token("*")
        };
        
        boolean parseStatus = syntaxAnalyser.analyse(inputTokens);

        assertFalse(parseStatus);
    }

    // @Test
    // public void basicGrammar() {
    //     GrammarParts parts = GrammarGenerator.generateParts(Grammar.IntegerComputation);
    //     SyntaxAnalyser syntaxAnalyser = new syntaxAnalyser.LR0Parser(parts.tokens(),
    //                                                                 parts.nonTerminals(),
    //                                                                 parts.productionRules(),
    //                                                                 parts.sentinal());
    //     assertTrue(false);
    // }
}
