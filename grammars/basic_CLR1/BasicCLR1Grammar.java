package grammars.basic_CLR1;

import java.util.*;

import grammar_objects.*;

public class BasicCLR1Grammar extends Grammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("a"));
        tokens.add(new Token("b"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("S");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("S"));
        nonTerminals.add(new NonTerminal("X"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("S"), 
            new LexicalElement[] {
                new NonTerminal("X"),
                new NonTerminal("X")
            }
        ));

        productionRules.add(new ProductionRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("X")
            }
        ));

        productionRules.add(new ProductionRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("b")
            }
        ));
    }
}
