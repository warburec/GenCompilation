package tests.testAids.GrammarGenerators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import GrammarObjects.GrammarPosition;
import GrammarObjects.LexicalElement;
import GrammarObjects.NonTerminal;
import GrammarObjects.ProductionRule;
import GrammarObjects.Route;
import GrammarObjects.State;
import GrammarObjects.Token;

public class TestGrammar extends Grammar {
    
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
    }

    public Set<State> getStates() {
        ProductionRule extraRootRule = new ProductionRule(null, new LexicalElement[] {sentinal});

        List<State> expectedStates = new ArrayList<>();
        expectedStates.add(new State(
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
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(productionRules[1], 1),
                new GrammarPosition(productionRules[0], 1),
            }),
            expectedStates.get(0)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            expectedStates.get(1)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[1], 3),
            }),
            expectedStates.get(2)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 2),
                new GrammarPosition(productionRules[3], 0),
                new GrammarPosition(productionRules[4], 0),
            }),
            expectedStates.get(1)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[0], 3),
            }),
            expectedStates.get(3)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[2], 1),
            }),
            expectedStates.get(0)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[3], 1),
            }),
            expectedStates.get(0)
        ));
        expectedStates.add(new State(
            Set.of(new GrammarPosition[] {
                new GrammarPosition(productionRules[4], 1),
            }),
            expectedStates.get(0)
        ));

        //Tree branches
        expectedStates.get(0)
            .addBranch(new Route(expectedStates.get(1), new NonTerminal("E")))
            .addBranch(new Route(expectedStates.get(6), new NonTerminal("B")))
            .addBranch(new Route(expectedStates.get(7), new Token("0")))
            .addBranch(new Route(expectedStates.get(8), new Token("1")));
        expectedStates.get(1)
            .addBranch(new Route(expectedStates.get(4), new Token("+")))
            .addBranch(new Route(expectedStates.get(2), new Token("*")));
        expectedStates.get(2)
            .addBranch(new Route(expectedStates.get(3), new NonTerminal("B")));
        expectedStates.get(4)
            .addBranch(new Route(expectedStates.get(5), new NonTerminal("B")));

        //Graph branches, links to existing states
        expectedStates.get(2)
            .addBranch(new Route(expectedStates.get(7), new Token("0")))
            .addBranch(new Route(expectedStates.get(8), new Token("1")));
        expectedStates.get(4)
            .addBranch(new Route(expectedStates.get(7), new Token("0")))
            .addBranch(new Route(expectedStates.get(8), new Token("1")));

        return new HashSet<>(expectedStates);
    }

    public Map<> getActionTable() {
        return new Map<State, Action>();
        //Action(ActionOperation)
        //ActionOperation -> Shift(Map<Token, State>)
    }

    public Map<> getGotoTable() {
        return new Map<State, Map<NonTerminal, State>>();
    }
}
