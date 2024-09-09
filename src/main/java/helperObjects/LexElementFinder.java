package helperObjects;

import java.util.HashSet;
import java.util.Set;

import grammar_objects.*;

public class LexElementFinder {
    
    public static Set<Token> tokens(Set<ProductionRule> productionRules) {
        ProductionRule[] ruleArray = new ProductionRule[productionRules.size()];
        return tokens(productionRules.toArray(ruleArray));
    }

    public static Set<Token> tokens(ProductionRule[] productionRules) {
        Set<Token> tokens = new HashSet<>();

        for (ProductionRule rule : productionRules) {
            for (LexicalElement element : rule.productionSequence()) {
                if(element instanceof Token) {
                    tokens.add((Token) element);
                }
            }
        }

        return tokens;
    }

    public static Set<NonTerminal> nonTerminals(Set<ProductionRule> productionRules) {
        ProductionRule[] ruleArray = new ProductionRule[productionRules.size()];
        return nonTerminals(productionRules.toArray(ruleArray));
    }
    
    public static Set<NonTerminal> nonTerminals(ProductionRule[] productionRules) {
        Set<NonTerminal> nonTerminals = new HashSet<>();

        for (ProductionRule rule : productionRules) {
            nonTerminals.add(rule.nonTerminal());
        }

        return nonTerminals;
    }

}
