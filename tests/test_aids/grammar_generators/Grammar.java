package tests.test_aids.grammar_generators;

import java.util.*;

import grammar_objects.*;
import tests.test_aids.GrammarParts;

public abstract class Grammar {
    
    protected List<Token> tokens = new ArrayList<>();
    protected List<NonTerminal> nonTerminals = new ArrayList<>();
    protected List<ProductionRule> productionRules = new ArrayList<>();
    protected NonTerminal sentinal;
    
    public Grammar() {}

    public GrammarParts getParts() {
        return new GrammarParts(
                        Set.copyOf(tokens), 
                        Set.copyOf(nonTerminals), 
                        Set.copyOf(productionRules), 
                        sentinal
                );
    }

}
