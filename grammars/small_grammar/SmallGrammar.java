package grammars.small_grammar;

import java.util.*;

import grammar_objects.*;

/**
 * E->E+B
 * E->E*B
 * E->B
 * B->0
 * B->1
 */
public class SmallGrammar extends Grammar {

    @Override
    protected  void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("0"));
        tokens.add(new Token("1"));
        tokens.add(new Token("*"));
        tokens.add(new Token("+"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("E");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("E"));
        nonTerminals.add(new NonTerminal("B"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("+"),
                new NonTerminal("B")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("*"),
                new NonTerminal("B")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("B")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("0")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("1")
            }));
    }

}
