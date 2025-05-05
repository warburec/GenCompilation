package test_aids.test_grammars;

import java.util.*;

import grammar_objects.*;
import grammars.basic_CLR1.BasicCLR1Grammar;
import syntax_analysis.grammar_structure_creation.*;
import test_aids.*;

public class BasicCLR1TestGrammar {

    private Grammar grammar = BasicCLR1Grammar.produce();
    private List<State> states;

    public TestGrammar getGrammar() {
        TestGrammarBuilder builder = new TestGrammarBuilder(grammar);

        states = gatherStates(builder.extraRootRule);

        return builder.setUp()
        .addStates(gatherStates(builder.extraRootRule))
        .selectState(states.get(0))
            .addBranch(new Route(states.get(1), new NonTerminal("S")))
            .addBranch(new Route(states.get(2), new NonTerminal("X")))
            .addBranch(new Route(states.get(7), new Token("a")))
            .addBranch(new Route(states.get(9), new Token("b")))
            .deselectState()

        .selectState(states.get(2))
            .addBranch(new Route(states.get(3), new NonTerminal("X")))
            .addBranch(new Route(states.get(4), new Token("a")))
            .addBranch(new Route(states.get(6), new Token("b")))
            .deselectState()

        .selectState(states.get(4))
            .addBranch(new Route(states.get(5), new NonTerminal("X")))
            .addBranch(new Route(states.get(4), new Token("a")))
            .addBranch(new Route(states.get(6), new Token("b")))
            .deselectState()
        
        .selectState(states.get(7))
            .addBranch(new Route(states.get(8), new NonTerminal("X")))
            .addBranch(new Route(states.get(7), new Token("a")))
            .addBranch(new Route(states.get(9), new Token("b")))
            .deselectState()
        
        .commitStates()
        .commitTables()
        .commitParseTrees()
        .commitRuleConvertors()
        .commitCodeGenerations()
        .generateTestGrammar();

    }

    private List<State> gatherStates(ProductionRule extraRootRule) {
        states = new ArrayList<State>();

        states.add(new State( //0
            Set.of(new GrammarPosition[] {
                new CLR1Position(extraRootRule, 0, Set.of(new EOF())),
                new CLR1Position(getRule(0), 0, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
                new CLR1Position(getRule(2), 0, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
            }),
            null
        ));

        states.add(new State( //1
            Set.of(new GrammarPosition[] {
                new CLR1Position(extraRootRule, 1, Set.of(new EOF())),
            }),
            states.get(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(0), 1, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
            }),
            states.get(0)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(0), 2, Set.of(new EOF())),
            }),
            states.get(2)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 1, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
            }),
            states.get(2)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 2, Set.of(new EOF())),
            }),
            states.get(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(2), 1, Set.of(new EOF())),
            }),
            states.get(4)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 1, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
                new CLR1Position(getRule(1), 0, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
                new CLR1Position(getRule(2), 0, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
            }),
            states.get(0)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 2, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
            }),
            states.get(7)
        ));

        states.add(new State( //9
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(2), 1, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
            }),
            states.get(7)
        ));

        return states;
    }

    private ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }

}
