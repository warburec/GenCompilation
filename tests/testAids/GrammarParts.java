package tests.testAids;

import GrammarObjects.*;

public record GrammarParts(Token[] tokens, NonTerminal[] nonTerminals, ProductionRule[] productionRules, NonTerminal sentinal) {}
