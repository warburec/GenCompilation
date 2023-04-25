package tests.testAids.GrammarGenerators;

import java.util.*;

import grammar_objects.*;
import tests.testAids.GrammarParts;

public abstract class Grammar {
    
    List<Token> tokens = new ArrayList<>();
    List<NonTerminal> nonTerminals = new ArrayList<>();
    List<ProductionRule> productionRules = new ArrayList<>();
    NonTerminal sentinal;
    
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
