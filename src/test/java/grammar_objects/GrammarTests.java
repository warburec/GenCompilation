package grammar_objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GrammarTests {
    
    @Test
    public void fullEquality() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        assertEquals(grammar1, grammar2);
    }

    @Test
    public void lessTokens() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("a"));
            }

            @Override
            protected NonTerminal setUpSentinal() {
                return new NonTerminal("A");
            }

            @Override
            protected void setUpNonTerminals(NonTerminalOrganiser nonTerminalOrganiser) {
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("A"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void moreTokens() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
            @Override
            protected void setUpTokens(TokenOrganiser tokenOrganiser) {
                tokenOrganiser.addToken(new Token("a"));
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        assertNotEquals(grammar1, grammar2);
    }
    
    @Test
    public void lessNonTerminals() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
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
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void moreNonTerminals() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("C"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void lessRules() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));
            }
        };

        assertNotEquals(grammar1, grammar2);
    }

    @Test
    public void moreRules() {
        Grammar grammar1 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
                    new LexicalElement[] {
                        new Token("b")
                    }
                ));
            }
        };

        Grammar grammar2 = new Grammar() {
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
                nonTerminalOrganiser.addNonTerminal(new NonTerminal("B"));
            }

            @Override
            protected void setUpProductionRules(RuleOrganiser ruleOrganiser) {
                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("A"), 
                    new LexicalElement[] {
                        new Token("a"),
                        new NonTerminal("B")
                    }
                ));

                ruleOrganiser.addRule(new ProductionRule(
                    new NonTerminal("B"), 
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

        assertNotEquals(grammar1, grammar2);
    }
}
