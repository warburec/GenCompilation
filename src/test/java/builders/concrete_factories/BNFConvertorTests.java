package builders.concrete_factories;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import builders.concrete_factories.BNFConvertor.InvalidEscapeCharacterException;
import grammar_objects.*;

public class BNFConvertorTests {

    @Test
    public void singleRule() {
        String bnf  = """
            A -> b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void multipleRules() {
        String bnf  = """
            A -> b
            B -> c
            C -> d
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("c"));
                tokenOrganiser.addToken(new Token("d"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("C"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("C"), 
                    new LexicalElement[] {
                        new Token("d")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void multipleTokens() {
        String bnf  = """
            A -> a b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("a"));
                tokenOrganiser.addToken(new Token("b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new Token("b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void nonTerminalInRule() {
        String bnf  = """
            A -> N b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("N"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new NonTerminal("N"),
                        new Token("b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void taggedTokens() {
        String bnf  = """
            A -> t:b t:B
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("B"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b"),
                        new Token("B")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void taggedNonTerminals() {
        String bnf  = """
            n:a -> n:b n:B
            n:b -> c
            B -> d
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("c"));
                tokenOrganiser.addToken(new Token("d"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("a");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("a"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("b"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("a"), 
                    new LexicalElement[] {
                        new NonTerminal("b"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("b"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("d")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void tagAsTokenPrefix() {
        String bnf  = """
            A -> t:t:b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("t:b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("t:b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void tagAsNonTerminalPrefix() {
        String bnf  = """
            n:A -> n:n:B
            n:n:B -> c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("n:B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new NonTerminal("n:B"),
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("n:B"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedSpaceTokens() {
        String bnf  = """
            A -> \\  b\\  t:\\ \\\s
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token(" "));
                tokenOrganiser.addToken(new Token("b "));
                tokenOrganiser.addToken(new Token("  "));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token(" "),
                        new Token("b "),
                        new Token("  ")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedSpaceNonTerminal() {
        String bnf  = """
            n:\\ A -> B\\ \\  n:C\\ C
            B\\ \\  -> a
            n:C\\ C -> b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("a"));
                tokenOrganiser.addToken(new Token("b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal(" A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal(" A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B  "));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("C C"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal(" A"), 
                    new LexicalElement[] {
                        new NonTerminal("B  "),
                        new NonTerminal("C C")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B  "), 
                    new LexicalElement[] {
                        new Token("a")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("C C"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedNewlineTokens() {
        String bnf  = """
            A -> \\\n b\\\n t:\\\n\\\n
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("\n"));
                tokenOrganiser.addToken(new Token("b\n"));
                tokenOrganiser.addToken(new Token("\n\n"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("\n"),
                        new Token("b\n"),
                        new Token("\n\n")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedNewlineNonTerminal() {
        String bnf  = """
            n:\\\nA -> B\\\n\\\n n:C\\\nC
            B\\\n\\\n -> a
            n:C\\\nC -> b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("a"));
                tokenOrganiser.addToken(new Token("b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("\nA");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("\nA"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B\n\n"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("C\nC"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("\nA"), 
                    new LexicalElement[] {
                        new NonTerminal("B\n\n"),
                        new NonTerminal("C\nC")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B\n\n"), 
                    new LexicalElement[] {
                        new Token("a")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("C\nC"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void arrowInTokens() {
        String bnf  = """
            A -> -> t:a-> \\ ->\\\s
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("->"));
                tokenOrganiser.addToken(new Token("a->"));
                tokenOrganiser.addToken(new Token(" -> "));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("->"),
                        new Token("a->"),
                        new Token(" -> ")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void arrowInNonTerminals() {
        String bnf  = """
            n:->A -> N-> n:N-> b
            n:N-> -> c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("->A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("->A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("N->"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("->A"), 
                    new LexicalElement[] {
                        new NonTerminal("N->"),
                        new NonTerminal("N->"),
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("N->"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void emptyTokenUsage() {
        String bnf  = """
            A -> B C \\e b t:
            B -> t:
            C -> \\e
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new EmptyToken());
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("C"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new NonTerminal("B"),
                        new NonTerminal("C"),
                        new EmptyToken(),
                        new Token("b"),
                        new EmptyToken()
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new EmptyToken()
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("C"), 
                    new LexicalElement[] {
                        new EmptyToken()
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedSpaceBeforeArrow() {
        String bnf  = """
            A\\  -> b
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A ");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A "));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A "), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalOr() {
        String bnf  = """
            A -> b | c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalEscapedOr() {
        String bnf  = """
            A -> b \\| c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("|"));
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b"),
                        new Token("|"),
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }


    @Test
    public void validEscapes() {
        String bnf  = """
            A -> \\ \\\n \\\t \\e \\|
            """;
        
        assertDoesNotThrow(() -> new BNFConvertor(bnf));
    }
    
    @Test
    public void invalidEscape() {
        String bnf  = """
            A -> \\b
            """;
        
        assertThrows(InvalidEscapeCharacterException.class, () -> new BNFConvertor(bnf));
    }

    @Test
    public void singleNonTerminalAlternativesWithoutOr() {
        String bnf  = """
            A -> b
            A -> c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalAlternativesMultiLine() {
        String bnf  = """
            A -> b
            | c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalAlternativesMultiLineVariableSpacing() {
        String bnf  = """
            A -> b
             | c
                | d
            \t| e
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new Token("c"));
                tokenOrganiser.addToken(new Token("d"));
                tokenOrganiser.addToken(new Token("e"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("d")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("e")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalAlternativesEmptyLine() {
        String bnf  = """
            A -> b
            |
            | c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new EmptyToken());
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new EmptyToken()
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalEmptyInlineOr() {
        String bnf  = """
            A -> b | | c
            """;
        
        Grammar expectedGrammar = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("b"));
                tokenOrganiser.addToken(new EmptyToken());
                tokenOrganiser.addToken(new Token("c"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new EmptyToken()
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("c")
                    }
                ));
            }
        };

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }
}