package tests.testAids.GrammarGenerators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import GrammarObjects.Action;
import GrammarObjects.GrammarPosition;
import GrammarObjects.LexicalElement;
import GrammarObjects.NonTerminal;
import GrammarObjects.ProductionRule;
import GrammarObjects.Reduce;
import GrammarObjects.Route;
import GrammarObjects.Shift;
import GrammarObjects.State;
import GrammarObjects.Token;

public class TestGrammar extends Grammar {
    
    List<State> states = new ArrayList<>();

    public TestGrammar() {
        tokens = new Token[] {
            new Token("0"),
            new Token("1"),
            new Token("*"),
            new Token("+")
        };

        sentinal = new NonTerminal("E");
        nonTerminals = new NonTerminal[] {
            new NonTerminal("E"),
            new NonTerminal("B")
        };

        productionRules = new ProductionRule[] {
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("E"),
                                new Token("+"),
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("E"),
                                new Token("*"),
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("E"),
                            new LexicalElement[] {
                                new NonTerminal("B")
                            }),
            new ProductionRule(new NonTerminal("B"),
                            new LexicalElement[] {
                                new Token("0")
                            }),
            new ProductionRule(new NonTerminal("B"),
                            new LexicalElement[] {
                                new Token("1")
                            })
        };

        ProductionRule extraRootRule = new ProductionRule(null, new LexicalElement[] {sentinal});

        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(productionRules[1], 0),
                new GrammarPosition(productionRules[0], 0),
                new GrammarPosition(productionRules[2], 0),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            null
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(productionRules[1], 1),
                new GrammarPosition(productionRules[0], 1),
            }),
            states.get(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            states.get(1)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 3),
            }),
            states.get(2)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            states.get(1)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 3),
            }),
            states.get(3)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[2], 1),
            }),
            states.get(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[3], 1),
            }),
            states.get(0)
        ));
        states.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[4], 1),
            }),
            states.get(0)
        ));

        //Tree branches
        states.get(0)
            .addBranch(new Route(states.get(1), new NonTerminal("E")))
            .addBranch(new Route(states.get(6), new NonTerminal("B")))
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")));
        states.get(1)
            .addBranch(new Route(states.get(4), new Token("+")))
            .addBranch(new Route(states.get(2), new Token("*")));
        states.get(2)
            .addBranch(new Route(states.get(3), new NonTerminal("B")));
        states.get(4)
            .addBranch(new Route(states.get(5), new NonTerminal("B")));

        //Graph branches, links to existing states
        states.get(2)
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")));
        states.get(4)
            .addBranch(new Route(states.get(7), new Token("0")))
            .addBranch(new Route(states.get(8), new Token("1")));
    }

    public Set<State> getStates() {
        return new HashSet<>(states);
    }

    public Map<State, Action> getActionTable() {
        Map<State, Action> actionMap = new HashMap<State, Action>();
        Map<Token, State> currentStateActions = new HashMap<>();

        currentStateActions.put(new Token("0"), states.get(7));
        currentStateActions.put(new Token("1"), states.get(8));
        actionMap.put(states.get(0), new Shift(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Token("*"), states.get(2));
        currentStateActions.put(new Token("+"), states.get(4));
        // currentStateActions.put(new Token(null), states.get()); //TODO: Decide how Accept should be handled
        actionMap.put(states.get(1), new Shift(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        currentStateActions.put(new Token("0"), states.get(7));
        currentStateActions.put(new Token("1"), states.get(8));
        actionMap.put(states.get(2), new Shift(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        actionMap.put(states.get(3), new Reduce(productionRules[1]));
        
        currentStateActions.put(new Token("0"), states.get(7));
        currentStateActions.put(new Token("1"), states.get(8));
        actionMap.put(states.get(4), new Shift(new HashMap<>(currentStateActions)));
        currentStateActions.clear();
        
        actionMap.put(states.get(5), new Reduce(productionRules[0]));
        
        actionMap.put(states.get(6), new Reduce(productionRules[2]));

        actionMap.put(states.get(7), new Reduce(productionRules[3]));

        actionMap.put(states.get(8), new Reduce(productionRules[4]));

        return actionMap;
    }

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        Map<State, Map<NonTerminal, State>> gotoMap = new HashMap<>();
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();

        currentGotoActions.put(new NonTerminal("E"), states.get(1));
        currentGotoActions.put(new NonTerminal("B"), states.get(6));
        gotoMap.put(states.get(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("B"), states.get(5));
        gotoMap.put(states.get(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("B"), states.get(3));
        gotoMap.put(states.get(2), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        return gotoMap;
    }
}
