package tests.testAids.GrammarGenerators;

import java.util.*;

import grammar_objects.NonTerminal;
import grammar_objects.ProductionRule;
import grammar_objects.Token;
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
