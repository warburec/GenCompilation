package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;

/**
 * S –> AA    
 * A –> aA
 * A –> b
 */
public class BasicSLR1Grammar extends Grammar implements SLR1TestGrammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("a"));
        tokens.add(new Token("b"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("S");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("S"));
        nonTerminals.add(new NonTerminal("A"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("S"),
            new LexicalElement[] {
                new NonTerminal("A"),
                new NonTerminal("A")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("A")
            }
        ));
        productionRules.add(new ProductionRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
            }
        ));
    }

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
        states.add(new State(
            Set.of(
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(0), 0),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            null
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(extraRootRule, 1)
            ),
            getState(0)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(0), 1),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            getState(0)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(0), 2)
            ),
            getState(2)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            getState(2)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(1), 2)
            ),
            getState(4)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(2), 1)
            ),
            getState(4)
        ));

        getState(0).addBranch(new Route(getState(1), new NonTerminal("S")));
        getState(0).addBranch(new Route(getState(2), new NonTerminal("A")));
        getState(0).addBranch(new Route(getState(4), new Token("a")));
        getState(0).addBranch(new Route(getState(6), new Token("b")));

        getState(2).addBranch(new Route(getState(3), new NonTerminal("A")));
        getState(2).addBranch(new Route(getState(4), new Token("a")));
        getState(2).addBranch(new Route(getState(6), new Token("b")));

        getState(4).addBranch(new Route(getState(5), new NonTerminal("A")));
        getState(4).addBranch(new Route(getState(4), new Token("a")));
        getState(4).addBranch(new Route(getState(6), new Token("b")));
    }

    @Override
    protected void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap) {
        
    }

    @Override
    protected void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {
        
    }

    @Override
    protected void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations) {
        
    }

    @Override
    public Map<State, Map<Token, Action>> getSLR1ActionTable() {
        Map<State, Map<Token, Action>> actionTable = new HashMap<>();
        for (State state : getStates()) {
            actionTable.put(state, new HashMap<>());
        }

        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Token("a"), new Shift(getState(4)));
        stateActions.put(new Token("b"), new Shift(getState(6)));

        //Accept EOF at state 1
        stateActions = actionTable.get(getState(1));
        stateActions.put(new EOF(), new Accept());

        stateActions = actionTable.get(getState(2));
        stateActions.put(new Token("a"), new Shift(getState(4)));
        stateActions.put(new Token("b"), new Shift(getState(6)));

        stateActions = actionTable.get(getState(3));
        stateActions.put(new EOF(), new Reduction(getRule(0)));

        stateActions = actionTable.get(getState(4));
        stateActions.put(new Token("a"), new Shift(getState(4)));
        stateActions.put(new Token("b"), new Shift(getState(6)));

        stateActions = actionTable.get(getState(5));
        stateActions.put(new Token("a"), new Reduction(getRule(1)));
        stateActions.put(new Token("b"), new Reduction(getRule(1)));
        stateActions.put(new EOF(), new Reduction(getRule(1)));

        stateActions = actionTable.get(getState(6));
        stateActions.put(new Token("a"), new Reduction(getRule(2)));
        stateActions.put(new Token("b"), new Reduction(getRule(2)));
        stateActions.put(new EOF(), new Reduction(getRule(2)));

        return actionTable;
    }

    @Override
    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();
        for (State state : getStates()) {
            gotoTable.put(state, new HashMap<>());
        }

        Map<NonTerminal, State> currentGotoActions = gotoTable.get(getState(0));
        currentGotoActions.put(new NonTerminal("S"), getState(1));
        currentGotoActions.put(new NonTerminal("A"), getState(2));

        currentGotoActions = gotoTable.get(getState(2));
        currentGotoActions.put(new NonTerminal("A"), getState(3));

        currentGotoActions = gotoTable.get(getState(4));
        currentGotoActions.put(new NonTerminal("A"), getState(5));

        return gotoTable;
    }
    
}
