package grammar_objects;

import java.util.*;

public abstract class Grammar {
    
    protected List<Token> tokens = new ArrayList<>();
    protected List<NonTerminal> nonTerminals = new ArrayList<>();
    protected List<ProductionRule> productionRules = new ArrayList<>();
    protected NonTerminal sentinal;
    
    public Grammar() {
        setUpTokens(tokens);
        sentinal = setUpSentinal();
        setUpNonTerminals(nonTerminals);
        setUpProductionRules(productionRules);
    }

    protected abstract void setUpTokens(List<Token> tokens);
    protected abstract NonTerminal setUpSentinal();
    protected abstract void setUpNonTerminals(List<NonTerminal> nonTerminals);
    protected abstract void setUpProductionRules(List<ProductionRule> productionRules);

    public ProductionRule getRule(int index) {
        return productionRules.get(index);
    }

    public GrammarParts getParts() {
        return new GrammarParts(
            Set.copyOf(tokens), 
            Set.copyOf(nonTerminals), 
            Set.copyOf(productionRules), 
            sentinal
        );
    }
}
