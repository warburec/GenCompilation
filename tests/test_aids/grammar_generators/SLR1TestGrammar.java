package tests.test_aids.grammar_generators;

import java.util.Map;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

public interface SLR1TestGrammar {
    public Map<State, Map<Token, Action>> getSLR1ActionTable();
    public Map<State, Map<NonTerminal, State>> getGotoTable();

    //TODO: Make more consistent with LR0 (make interface)
    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public abstract ParseState getParseRoot(String sentence);
}
