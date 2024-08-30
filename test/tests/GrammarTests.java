package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import grammar_objects.*;

public class GrammarTests {
    
    @Test
    public void equality() {
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

        Grammar producedGrammar = new Grammar() {
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

        assertEquals(expectedGrammar, producedGrammar);
    }
}
