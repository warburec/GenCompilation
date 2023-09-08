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

public class CLR1ParserTests {
    
    @Test
    public void nonTerminalNull() {
        Token[] tokens = new Token[] {};
        NonTerminal sentinal = null;
        NonTerminal[] nonTerminals = new NonTerminal[] {
            null
        };
        ProductionRule[] productionRules = new ProductionRule[] {};

        assertThrows(RuntimeException.class, () -> new CLR1Parser(tokens,
                                                            nonTerminals,
                                                            productionRules,
                                                            sentinal));
    }

    @Test
    public void smallTestGrammarStates() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();

        CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        // assertEquals(expectedStateSet, generatedStates); //TODO: Create states and tables for CLR1, for all grammars
        assertTrue(generatedStates.size() >= expectedStateSet.size());
    }

    // @Test
    // public void smallTestGrammarAction() {
    //     SLR1TestGrammar grammar = new SmallTestGrammar();
    //     GrammarParts grammarParts = ((LR0TestGrammar)grammar).getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

    //     Map<State, Map<Token, Action>> expectedActionTable = grammar.getSLR1ActionTable();
    //     assertEquals(expectedActionTable, generatedActionTable);
    // }

    // @Test
    // public void smallTestGrammarGoto() {
    //     LR0TestGrammar grammar = new SmallTestGrammar();
    //     GrammarParts grammarParts = grammar.getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

    //     Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
    //     assertEquals(expectedGotoTable, generatedGotoTable);
    // }

    // @Test
    // public void parsingTestGrammarCompleteSentence() throws ParseFailedException {
    //     LR0TestGrammar grammar = new SmallTestGrammar();
    //     GrammarParts grammarParts = grammar.getParts();
    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     //1+0*1
    //     Token[] inputTokens = new Token[] {
    //         new Token("1"),
    //         new Token("+"),
    //         new Token("0"),
    //         new Token("*"),
    //         new Token("1")
    //     };
        
    //     ParseState generatedParseRoot = syntaxAnalyser.analyse(inputTokens);

    //     ParseState expectedParseRoot = grammar.getParseRoot("1+0*1");
    //     assertEquals(expectedParseRoot, generatedParseRoot);
    // }

    @Test
    public void parsingTestGrammarIncompleteSentence() throws ParseFailedException {
        LR0TestGrammar grammar = new SmallTestGrammar();
        GrammarParts grammarParts = grammar.getParts();
        CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
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
        CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
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
        SelfReferentialGrammar grammar = new SelfReferentialGrammar();
        GrammarParts grammarParts = grammar.getParts();

        CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getCLR1States();

        for(State s : generatedStates) {
            assertTrue(expectedStateSet.contains(s));
        }

        assertEquals(expectedStateSet, generatedStates);
    }

    // @Test
    // public void selfReferentialGrammarAction() {
    //     SLR1TestGrammar grammar = new SelfReferentialGrammar();
    //     GrammarParts grammarParts = ((LR0TestGrammar)grammar).getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

    //     Map<State, Map<Token, Action>> expectedActionTable = grammar.getSLR1ActionTable();
    //     assertEquals(expectedActionTable, generatedActionTable);
    // }

    // @Test
    // public void selfReferentialGrammarGoto() {
    //     LR0TestGrammar grammar = new SelfReferentialGrammar();
    //     GrammarParts grammarParts = grammar.getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

    //     Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
    //     assertEquals(expectedGotoTable, generatedGotoTable);
    // }

    @Test
    public void basicIdentifierGrammarStates() {
        LR0TestGrammar grammar = new BasicIdentifierGrammar();
        GrammarParts grammarParts = grammar.getParts();

        CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        // assertEquals(expectedStateSet, generatedStates);
        assertTrue(generatedStates.size() >= expectedStateSet.size());
    }

    // @Test
    // public void basicIdentifierGrammarAction() {
    //     SLR1TestGrammar grammar = new BasicIdentifierGrammar();
    //     GrammarParts grammarParts = ((LR0TestGrammar)grammar).getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

    //     Map<State, Map<Token, Action>> expectedActionTable = grammar.getSLR1ActionTable();
    //     assertEquals(expectedActionTable, generatedActionTable);
    // }

    // @Test
    // public void basicIdentifierGrammarGoto() {
    //     LR0TestGrammar grammar = new BasicIdentifierGrammar();
    //     GrammarParts grammarParts = grammar.getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

    //     Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
    //     assertEquals(expectedGotoTable, generatedGotoTable);
    // }

    // @Test
    // public void XToYToXGrammarCompleteSentence() throws ParseFailedException {
    //     LR0TestGrammar grammar = new BasicIdentifierGrammar();
    //     GrammarParts grammarParts = grammar.getParts();
    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Token[] inputTokens = new Token[] {
    //         new Identifier("identifier", null, "x"),
    //         new Token("="),
    //         new Literal("number", "1"),
    //         new Token("+"),
    //         new Literal("number", "2"),
    //         new Token(";"),
    //         new Identifier("identifier", null, "y"),
    //         new Token("="),
    //         new Identifier("identifier", null, "x"),
    //         new Token("+"),
    //         new Literal("number", "3"),
    //         new Token(";"),
    //         new Identifier("identifier", null, "x"),
    //         new Token("="),
    //         new Identifier("identifier", null, "y"),
    //         new Token("+"),
    //         new Literal("number", "0"),
    //         new Token(";")
    //     };
        
    //     ParseState generatedParseRoot = syntaxAnalyser.analyse(inputTokens);

    //     ParseState expectedParseRoot = grammar.getParseRoot("XToYToX");
    //     assertEquals(expectedParseRoot, generatedParseRoot);
    // }

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
            new CLR1Parser(productionRules, sentinel);
        });
    }

    @Test
    public void basicSLR1GrammarStates() {
        Grammar grammar = new BasicSLR1Grammar();
        GrammarParts grammarParts = ((Grammar)grammar).getParts();

        CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
                                                grammarParts.nonTerminals(),
                                                grammarParts.productionRules(),
                                                grammarParts.sentinal());
        Set<State> generatedStates = syntaxAnalyser.getStates();

        Set<State> expectedStateSet = grammar.getStates();
        // assertEquals(expectedStateSet, generatedStates);
        assertTrue(generatedStates.size() >= expectedStateSet.size());
    }

    // @Test
    // public void basicSLR1GrammarAction() {
    //     SLR1TestGrammar grammar = new BasicSLR1Grammar();
    //     GrammarParts grammarParts = ((Grammar)grammar).getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<Token, Action>> generatedActionTable = syntaxAnalyser.getActionTable();

    //     Map<State, Map<Token, Action>> expectedActionTable = grammar.getSLR1ActionTable();
    //     assertEquals(expectedActionTable, generatedActionTable);
    // }

    // @Test
    // public void basicSLR1GrammarGoto() {
    //     SLR1TestGrammar grammar = new BasicSLR1Grammar();
    //     GrammarParts grammarParts = ((Grammar)grammar).getParts();

    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Map<State, Map<NonTerminal, State>> generatedGotoTable = syntaxAnalyser.getGotoTable();

    //     Map<State, Map<NonTerminal, State>> expectedGotoTable = grammar.getGotoTable();
    //     assertEquals(expectedGotoTable, generatedGotoTable);
    // }

    // @Test
    // public void basicSLR1GrammarCompleteSentence() throws ParseFailedException {
    //     SLR1TestGrammar grammar = new BasicSLR1Grammar();
    //     GrammarParts grammarParts = ((Grammar)grammar).getParts();
    //     CLR1Parser syntaxAnalyser = new CLR1Parser(grammarParts.tokens(),
    //                                             grammarParts.nonTerminals(),
    //                                             grammarParts.productionRules(),
    //                                             grammarParts.sentinal());
    //     Token[] inputTokens = new Token[] {
    //         new Token("b"),
    //         new Token("a"),
    //         new Token("a"),
    //         new Token("a"),
    //         new Token("b")
    //     };
        
    //     ParseState generatedParseRoot = syntaxAnalyser.analyse(inputTokens);

    //     ParseState expectedParseRoot = grammar.getParseRoot("CompleteSentence");
    //     assertEquals(expectedParseRoot, generatedParseRoot);
    // }

    @Test
    public void CLR1Grammar() {
        ProductionRule[] productionRules = new ProductionRule[] {
            new ProductionRule(
                new NonTerminal("S"),
                new LexicalElement[] {
                    new NonTerminal("X"),
                    new NonTerminal("X")
                }
            ),
            new ProductionRule(
                new NonTerminal("X"),
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("X")
                }
            ),
            new ProductionRule(
                new NonTerminal("X"),
                new LexicalElement[] {
                    new Token("b")
                }
            )
        };
        Token[] tokens = new Token[] {
            new Token("a"),
            new Token("b")
        };
        NonTerminal[] nonTerminals = new NonTerminal[] {
            new NonTerminal("S"),
            new NonTerminal("X")
        };
        NonTerminal sentinel = new NonTerminal("S");


        CLR1Parser syntaxAnalyser = new CLR1Parser(tokens,
                                                nonTerminals,
                                                productionRules,
                                                sentinel);
        
        //TODO: Ensure tables are correct (not that they just don't throw errors)
    }

    @Test
    public void CLR1BasicParsing() throws ParseFailedException {
        ProductionRule[] productionRules = new ProductionRule[] {
            new ProductionRule(
                new NonTerminal("S"),
                new LexicalElement[] {
                    new NonTerminal("X"),
                    new NonTerminal("X")
                }
            ),
            new ProductionRule(
                new NonTerminal("X"),
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("X")
                }
            ),
            new ProductionRule(
                new NonTerminal("X"),
                new LexicalElement[] {
                    new Token("b")
                }
            )
        };
        Token[] tokens = new Token[] {
            new Token("a"),
            new Token("b")
        };
        NonTerminal[] nonTerminals = new NonTerminal[] {
            new NonTerminal("S"),
            new NonTerminal("X")
        };
        NonTerminal sentinel = new NonTerminal("S");

        CLR1Parser syntaxAnalyser = new CLR1Parser(tokens,
                                                nonTerminals,
                                                productionRules,
                                                sentinel);
        
        Token[] sentence = new Token[] {
            new Token("a", 1, 1),
            new Token("a", 1, 2),
            new Token("b", 1, 3),
            new Token("a", 1, 4),
            new Token("a", 1, 5),
            new Token("a", 1, 6),
            new Token("b", 1, 7),
        };


        assertNotNull(syntaxAnalyser.analyse(sentence));
        //TODO: Ensure tables/parse states are correct (not that they just don't throw errors and aren't null)
    }
    //TODO: Make tests to parse sentences that would cause SLR1 issues
}