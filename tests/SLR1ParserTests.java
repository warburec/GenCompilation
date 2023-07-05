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

public class SLR1ParserTests {
    
    @Test
    public void nonTerminalNull() {
        Token[] tokens = new Token[] {};
        NonTerminal sentinal = null;
        NonTerminal[] nonTerminals = new NonTerminal[] {
            null
        };
        ProductionRule[] productionRules = new ProductionRule[] {};

        assertThrows(RuntimeException.class, () -> new SLR1Parser(tokens,
                                                            nonTerminals,
                                                            productionRules,
                                                            sentinal));
    }

    @Test
    public void smallTestGrammarStates() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void smallTestGrammarAction() {
        SLR1TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = ((LR0TestGrammar)grammar).getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Map<Token, Action>> expectedActionTable = grammar.getSLR1ActionTable();

        for (State state : expectedActionTable.keySet()) {
            Map<Token, Action> e = expectedActionTable.get(state);
            Map<Token, Action> g = generatedActionTable.get(state);

            for (Token token : e.keySet()) {
                assertEquals(e.get(token), g.get(token));
            }
        }

        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void smallTestGrammarGoto() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

        Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
        assertEquals(expectedGotoTable, generatedGotoTable);
    }

    @Test
    public void parsingTestGrammarCompleteSentence() throws ParseFailedException {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();
        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
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

        ParseState expectedParseRoot = grammar.getParseRoot("1+0*1");
        assertEquals(expectedParseRoot, generatedParseRoot);
    }

    @Test
    public void parsingTestGrammarIncompleteSentence() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();
        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
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
    public void parsingTestGrammarIncorrectSentence() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();
        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        //1+2*1
        Token[] inputTokens = new Token[] {
            new Token("1"),
            new Token("+"),
            new Token("2"),
            new Token("*"),
            new Token("1")
        };
        
        ParseFailedException exception = assertThrows(ParseFailedException.class, () -> syntaxAnalyser.analyse(inputTokens));
        assertTrue(exception.getCause() instanceof SyntaxError);
    }

    @Test
    public void selfReferentialGrammarStates() {
        LR0TestGrammar grammar = new SelfReferentialGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void selfReferentialGrammarAction() {
        LR0TestGrammar grammar = new SelfReferentialGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Map<Token, Action>> expectedActionTable = grammar.getActionTable();
        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void selfReferentialGrammarGoto() {
        LR0TestGrammar grammar = new SelfReferentialGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

        Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
        assertEquals(expectedGotoTable, generatedGotoTable);
    }

    @Test
    public void basicIdentifierGrammarStates() {
        LR0TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        assertEquals(expectedStateSet, generatedStates);
    }

    @Test
    public void basicIdentifierGrammarAction() {
        LR0TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Map<Token, Action>> expectedActionTable = grammar.getActionTable();
        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void basicIdentifierGrammarGoto() {
        LR0TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();

        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

        Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
        assertEquals(expectedGotoTable, generatedGotoTable);
    }

    @Test
    public void XToYToXGrammarCompleteSentence() throws ParseFailedException {
        LR0TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();
        SLR1Parser syntaxAnalyser = new SLR1Parser(grammarParts.tokens(),
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

        ParseState expectedParseRoot = grammar.getParseRoot("XToYToX");
        assertEquals(expectedParseRoot, generatedParseRoot);
    }

    @Test
    public void nonDeterminism() {
        ProductionRule[] productionRules = new ProductionRule[] {
            new ProductionRule(
                new NonTerminal("S"),
                new LexicalElement[] {
                    new NonTerminal("A")
                }
            ),
            new ProductionRule(
                new NonTerminal("S"),
                new LexicalElement[] {
                    new NonTerminal("B")
                }
            ),
            new ProductionRule(
                new NonTerminal("A"),
                new LexicalElement[] {
                    new Token("a")
                }
            ),
            new ProductionRule(
                new NonTerminal("B"),
                new LexicalElement[] {
                    new Token("a")
                }
            )
        };
        NonTerminal sentinel = new NonTerminal("S");

        assertThrows(NonDeterminismException.class, () -> {
            new SLR1Parser(productionRules, sentinel);
        });
    }
}
