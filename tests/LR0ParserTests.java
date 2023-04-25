package tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import syntax_analysis.*;
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
    public void parsingTestGrammarCompleteSentence() throws ParseFailedException {
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
        
        ParseState generatedParseRoot = syntaxAnalyser.analyse(inputTokens);

        ParseState expectedParseState = grammar.getParseRoot("1+0*1");
        assertEquals(expectedParseState, generatedParseRoot);
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
        
        ParseFailedException exception = assertThrows(ParseFailedException.class, () -> syntaxAnalyser.analyse(inputTokens));
        assertTrue(exception.getCause() instanceof IncompleteParseException);
    }
    
    //TODO: Test with grammar with a state that references itself
    /*
        H → h A
        A → a L
        L → l L //Self-referential
        L → o
     */

    //TODO: Add identifiers
}
