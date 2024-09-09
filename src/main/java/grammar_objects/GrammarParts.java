package grammar_objects;

import java.util.Set;

public record GrammarParts(
    Set<Token> tokens, 
    Set<NonTerminal> nonTerminals, 
    Set<ProductionRule> productionRules, 
    NonTerminal sentinal
) {}
