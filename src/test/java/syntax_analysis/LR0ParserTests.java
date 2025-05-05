package syntax_analysis;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import test_aids.*;
import test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;
import test_aids.test_grammars.self_referential.SelfReferentialTestGrammar;
import test_aids.test_grammars.small_grammar.SmallTestGrammar;

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
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
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
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Map<Token, Action>> expectedActionTable = grammar.getActionTable();
        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void smallTestGrammarGoto() {
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
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
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
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

        ParseState expectedParseRoot = grammar.getParseRoot("1+0*1");
        assertEquals(expectedParseRoot, generatedParseRoot);
    }

    @Test
    public void parsingTestGrammarIncompleteSentence() {
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
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
    public void parsingTestGrammarIncorrectSentence() {
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        GrammarParts grammarParts = grammar.getParts();
        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
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
        TestGrammar grammar = new SelfReferentialTestGrammar().getGrammar(GrammarType.LR0);
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
    public void selfReferentialGrammarAction() {
        TestGrammar grammar = new SelfReferentialTestGrammar().getGrammar(GrammarType.LR0);
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Map<Token, Action>> expectedActionTable = grammar.getActionTable();
        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void selfReferentialGrammarGoto() {
        TestGrammar grammar = new SelfReferentialTestGrammar().getGrammar(GrammarType.LR0);
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
    public void basicIdentifierGrammarStates() {
        TestGrammar grammar = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0);
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
    public void basicIdentifierGrammarAction() {
        TestGrammar grammar = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0);
        GrammarParts grammarParts = grammar.getParts();

        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

        Map<State, Map<Token, Action>> expectedActionTable = grammar.getActionTable();
        assertEquals(expectedActionTable, generatedActionTable);
    }

    @Test
    public void basicIdentifierGrammarGoto() {
        TestGrammar grammar = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0);
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
        TestGrammar grammar = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0);
        GrammarParts grammarParts = grammar.getParts();
        LR0Parser syntaxAnalyser = new LR0Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Token[] inputTokens = new Token[] {
            new Identifier("identifier", "x"),
            new Token("="),
            new Literal("number", "1"),
            new Token("+"),
            new Literal("number", "2"),
            new Token(";"),
            new Identifier("identifier", "y"),
            new Token("="),
            new Identifier("identifier", "x"),
            new Token("+"),
            new Literal("number", "3"),
            new Token(";"),
            new Identifier("identifier", "x"),
            new Token("="),
            new Identifier("identifier", "y"),
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
            new LR0Parser(productionRules, sentinel);
        });
    }
}
