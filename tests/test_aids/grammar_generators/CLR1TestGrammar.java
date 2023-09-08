package tests.test_aids.grammar_generators;

import java.util.*;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

public interface CLR1TestGrammar {
    public Map<State, Map<Token, Action>> getCLR1ActionTable();
    public Map<State, Map<NonTerminal, State>> getGotoTable();
    public List<State> clr1States = new ArrayList<>();

    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public abstract ParseState getParseRoot(String sentence);

    public abstract Set<State> getCLR1States();
    abstract void setUpCLR1States(List<State> states, ProductionRule extraRootRule);
}