package tests.test_aids.grammar_generators;

import java.util.Map;

import grammar_objects.Token;
import syntax_analysis.grammar_structure_creation.*;

public interface SLR1TestGrammar {
    public Map<State, Map<Token, Action>> getSLR1ActionTable();
}
