package grammars.self_referential;

import java.util.*;

import grammar_objects.*;

/**
 * H → h A
 * A → a L
 * L → l L //Self-referential
 * L → o
 */
public class SelfReferentialGrammar extends Grammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("h"));
        tokens.add(new Token("a"));
        tokens.add(new Token("l"));
        tokens.add(new Token("o"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("H");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("H"));
        nonTerminals.add(new NonTerminal("A"));
        nonTerminals.add(new NonTerminal("L"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(
            new ProductionRule(
                new NonTerminal("H"),
                new LexicalElement[] {
                    new Token("h"),
                    new NonTerminal("A")
                }));
        
        productionRules.add(
            new ProductionRule(
                new NonTerminal("A"),
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("L")
                }));
        
        productionRules.add(
            new ProductionRule(
                new NonTerminal("L"),
                new LexicalElement[] {
                    new Token("l"),
                    new NonTerminal("L")
                }));
        
        productionRules.add(
            new ProductionRule(
                new NonTerminal("L"),
                new LexicalElement[] {
                    new Token("o")
                }));
    }

}
