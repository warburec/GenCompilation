package tests.test_aids.grammar_generators;

import java.util.*;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

//TODO: Update for consistancy with SLR1TestGrammar
public abstract class LR0TestGrammar extends Grammar {
    private List<State> states = new ArrayList<>();

    private Map<State, Map<Token, Action>> actionTable = new HashMap<>();
    private Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();

    public LR0TestGrammar() {
        setUpStates(states, new ProductionRule(null, new LexicalElement[] {sentinal}));

        for (State state : states) {
            actionTable.put(state, new HashMap<>());
            gotoTable.put(state, new HashMap<>());
        }
        setUpActionTable(actionTable, new EOF());
        setUpGotoTable(gotoTable);
    }


    protected abstract void setUpStates(List<State> states, ProductionRule extraRootRule);

    public Set<State> getStates() {
        return new HashSet<>(states);
    }

    protected State getState(int index) {
        return states.get(index);
    }


    protected abstract void setUpActionTable(Map<State, Map<Token, Action>> actionTable, Token endOfFile);

    public Map<State, Map<Token, Action>> getActionTable() {
        return actionTable;
    }

    protected abstract void setUpGotoTable(Map<State, Map<NonTerminal, State>> gotoTable);

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        return gotoTable;
    }


    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public abstract ParseState getParseRoot(String sentence);

}