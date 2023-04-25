package tests.testAids.GrammarGenerators;

import java.util.*;

import grammar_objects.*;
import tests.testAids.GrammarParts;

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
