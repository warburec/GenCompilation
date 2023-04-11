package tests.testAids.GrammarGenerators;

import java.util.*;

import GrammarObjects.*;
import tests.testAids.GrammarParts;

public abstract class Grammar {
    
    Set<Token> tokens;
    Set<NonTerminal> nonTerminals;
    Set<ProductionRule> productionRules;
    NonTerminal sentinal;
    
    public Grammar() {
        tokens = new HashSet<>();
        nonTerminals = new HashSet<>();
        productionRules = new HashSet<>();
    }

    public GrammarParts getParts() {
        return new GrammarParts(tokens, nonTerminals, productionRules, sentinal);
    }

}
