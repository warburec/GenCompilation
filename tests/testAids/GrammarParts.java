package tests.testAids;

import java.util.Set;

import grammar_objects.NonTerminal;
import grammar_objects.ProductionRule;
import grammar_objects.Token;
import grammar_objects.*;

public record GrammarParts(Set<Token> tokens, Set<NonTerminal> nonTerminals, Set<ProductionRule> productionRules, NonTerminal sentinal) {}
