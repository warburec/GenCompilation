package tests.testAids;

import java.util.Set;

import GrammarObjects.*;

public record GrammarParts(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinal) {}
