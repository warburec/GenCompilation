package tests.testAids.GrammarGenerators;

import java.util.ArrayList;
import java.util.List;

import GrammarObjects.*;
import tests.testAids.GrammarParts;

public abstract class Grammar {
    
    List<Token> tokens;
    List<NonTerminal> nonTerminals;
    List<ProductionRule> productionRules;
    NonTerminal sentinal;
    
    public Grammar() {
        tokens = new ArrayList<>();
        nonTerminals = new ArrayList<>();
        productionRules = new ArrayList<>();
    }

    public GrammarParts getParts() {
        return new GrammarParts(tokens, nonTerminals, productionRules, sentinal);
    }

}
