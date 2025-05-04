package test_aids;

import java.util.*;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

public class TestGrammar extends Grammar {
    
    protected List<State> states = new ArrayList<>();
    protected Map<State, Map<Token, Action>> actionTable = new HashMap<>();
    protected Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();
    protected Map<String, RuleConvertor> ruleConvertors;
    protected Map<String, String> codeGenerations;
    protected Map<String, ParseState> parseRoots;

    public TestGrammar(
        Set<Token> tokens,
        Set<NonTerminal> nonTerminals,
        List<ProductionRule> productionRules,
        NonTerminal sentinal,
        List<State> states,
        Map<State, Map<Token, Action>> actionTable,
        Map<State, Map<NonTerminal, State>> gotoTable,
        Map<String, RuleConvertor> ruleConvertors,
        Map<String, String> codeGenerations,
        Map<String, ParseState> parseRoots
    ) {
        super(tokens, nonTerminals, productionRules, sentinal);
        this.states = states;
        this.actionTable = actionTable;
        this.gotoTable = gotoTable;
        this.ruleConvertors = ruleConvertors;
        this.codeGenerations = codeGenerations;
        this.parseRoots = parseRoots;
    }

    public Set<State> getStates() {
        return new HashSet<>(states);
    }

    /**
     * Gets a state populated in-order by setUpStates()
     * @param index The index of the state to be returned
     * @return The state found
     */
    protected State getState(int index) {
        return states.get(index);
    }

    public Map<State, Map<Token, Action>> getActionTable() {
        return actionTable;
    }

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        return gotoTable;
    }

    public ParseState getParseRoot(String sentenceName) {
        return parseRoots.get(sentenceName);
    }

    public RuleConvertor getRuleConvertor(String sentenceName) {
        return ruleConvertors.get(sentenceName);
    }

    public String getCodeGeneration(String sentenceName) {
        return codeGenerations.get(sentenceName);
    }
    
}
