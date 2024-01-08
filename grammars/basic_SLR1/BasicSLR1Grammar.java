package grammars.basic_SLR1;

import java.util.*;

import grammar_objects.*;

/**
 * S –> AA    
 * A –> aA
 * A –> b
 */
public class BasicSLR1Grammar extends Grammar {

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
        nonTerminals.add(new NonTerminal("A"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("S"),
            new LexicalElement[] {
                new NonTerminal("A"),
                new NonTerminal("A")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("A")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
            }
        ));
    }

}
