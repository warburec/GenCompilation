package tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import tests.test_aids.GrammarParts;
import tests.test_aids.grammar_generators.*;
import syntax_analysis.*;

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
        TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getGetState();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void smallTestGrammarAction() {
        TestGrammar grammar = new SmallTestGrammar();
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
        TestGrammar grammar = new SmallTestGrammar();
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
        TestGrammar grammar = new SmallTestGrammar();
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
        TestGrammar grammar = new SmallTestGrammar();
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

    @Test
    public void selfReferentialGrammarStates() {
        TestGrammar grammar = new SelfReferentialGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getGetState();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void selfReferentialGrammarAction() {
        TestGrammar grammar = new SelfReferentialGrammar();
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
    public void selfReferentialGrammarGoto() {
        TestGrammar grammar = new SelfReferentialGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

        Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
        assertEquals(expectedGotoTable, generatedGotoTable);
    }

    //TODO: Add identifiers
    @Test
    public void basicIdentifierGrammarStates() {
        TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getGetState();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void basicIdentifierGrammarAction() {
        TestGrammar grammar = new BasicIdentifierGrammar();
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
    public void basicIdentifierGrammarGoto() {
        TestGrammar grammar = new BasicIdentifierGrammar();
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
    public void XToYToXGrammarCompleteSentence() throws ParseFailedException {
        TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();
        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Token[] inputTokens = new Token[] {
            new Identifier("identifier", null, "x"),
            new Token("="),
            new Literal("number", "1"),
            new Token("+"),
            new Literal("number", "2"),
            new Token(";"),
            new Identifier("identifier", null, "y"),
            new Token("="),
            new Identifier("identifier", null, "x"),
            new Token("+"),
            new Literal("number", "3"),
            new Token(";"),
            new Identifier("identifier", null, "x"),
            new Token("="),
            new Identifier("identifier", null, "y"),
            new Token("+"),
            new Literal("number", "0"),
            new Token(";")
        };
        
        ParseState generatedParseRoot = syntaxAnalyser.analyse(inputTokens);

        ParseState expectedParseState = grammar.getParseRoot("XToYToX");
        assertEquals(expectedParseState, generatedParseRoot);
    }
}
