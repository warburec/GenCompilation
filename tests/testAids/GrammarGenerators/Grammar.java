package tests.testAids.GrammarGenerators;

import java.util.*;

import grammar_objects.fundamentals.*;
import tests.testAids.GrammarParts;

public abstract class Grammar {
    
    Token[] tokens;
    NonTerminal[] nonTerminals;
    ProductionRule[] productionRules;
    NonTerminal sentinal;
    
    public Grammar() {}

    public GrammarParts getParts() {
        return new GrammarParts(
                        Set.of(tokens), 
                        Set.of(nonTerminals), 
                        Set.of(productionRules), 
                        sentinal
                );
    }

}
