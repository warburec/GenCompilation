package tests.testAids;

import java.util.List;

import GrammarObjects.*;

public record GrammarParts(List<Token> tokens, List<NonTerminal> nonTerminals, List<ProductionRule> productionRules, NonTerminal sentinal) {}
