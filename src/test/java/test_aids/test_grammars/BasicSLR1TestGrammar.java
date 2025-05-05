package test_aids.test_grammars;

import java.util.*;

import grammar_objects.*;
import grammars.basic_SLR1.BasicSLR1Grammar;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;
import test_aids.*;

/**
 * S –> AA    
 * A –> aA
 * A –> b
 */
public class BasicSLR1TestGrammar {

    private Grammar grammar = BasicSLR1Grammar.produce();
    private List<State> states;

    public TestGrammar getGrammar() {
        TestGrammarBuilder builder = new TestGrammarBuilder(grammar);

        states = gatherStates(builder.extraRootRule);

        return builder.setUp()
        .addStates(states)
        .selectState(states.get(0))
            .addBranch(new Route(states.get(1), new NonTerminal("S")))
            .addBranch(new Route(states.get(2), new NonTerminal("A")))
            .addBranch(new Route(states.get(4), new Token("a")))
            .addBranch(new Route(states.get(6), new Token("b")))
            .deselectState()

        .selectState(states.get(2))
            .addBranch(new Route(states.get(3), new NonTerminal("A")))
            .addBranch(new Route(states.get(4), new Token("a")))
            .addBranch(new Route(states.get(6), new Token("b")))
            .deselectState()

        .selectState(states.get(4))
            .addBranch(new Route(states.get(5), new NonTerminal("A")))
            .addBranch(new Route(states.get(4), new Token("a")))
            .addBranch(new Route(states.get(6), new Token("b")))
            .deselectState()
        
        .commitStates()

        .selectState(states.get(0))
            .addAction(new Token("a"), new Shift(states.get(4)))
            .addAction(new Token("b"), new Shift(states.get(6)))
            .deselectState()

        //Accept EOF at state 1
        .selectState(states.get(1))
            .addAction(new EOF(), new Accept())
            .deselectState()

        .selectState(states.get(2))
            .addAction(new Token("a"), new Shift(states.get(4)))
            .addAction(new Token("b"), new Shift(states.get(6)))
            .deselectState()

        .selectState(states.get(3))
            .addAction(new EOF(), new Reduction(getRule(0)))
            .deselectState()

        .selectState(states.get(4))
            .addAction(new Token("a"), new Shift(states.get(4)))
            .addAction(new Token("b"), new Shift(states.get(6)))
            .deselectState()

        .selectState(states.get(5))
            .addAction(new Token("a"), new Reduction(getRule(1)))
            .addAction(new Token("b"), new Reduction(getRule(1)))
            .addAction(new EOF(), new Reduction(getRule(1)))
            .deselectState()

        .selectState(states.get(6))
            .addAction(new Token("a"), new Reduction(getRule(2)))
            .addAction(new Token("b"), new Reduction(getRule(2)))
            .addAction(new EOF(), new Reduction(getRule(2)))
            .deselectState()
        
        .selectState(states.get(0))
            .addGoto(new NonTerminal("S"), states.get(1))
            .addGoto(new NonTerminal("A"), states.get(2))
            .deselectState()

        .selectState(states.get(2))
            .addGoto(new NonTerminal("A"), states.get(3))
            .deselectState()

        .selectState(states.get(4))
            .addGoto(new NonTerminal("A"), states.get(5))
            .deselectState()
        
        .commitTables()

        .setParseTreeRoot("CompleteSentence", completeSentenceParse())

        .commitParseTrees()
        .commitRuleConvertors()
        .commitCodeGenerations()
        .generateTestGrammar();
    }

    private List<State> gatherStates(ProductionRule extraRootRule) {
        states = new ArrayList<State>();
        
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
            states.get(0)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(0), 1),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            states.get(0)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(0), 2)
            ),
            states.get(2)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(2), 0)
            ),
            states.get(2)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(1), 2)
            ),
            states.get(4)
        ));
        states.add(new State(
            Set.of(
                new GrammarPosition(getRule(2), 1)
            ),
            states.get(4)
        ));

        return states;
    }
    
     /**
     * Parse tree for the sentence "CompleteSentence"
     * @return The root ParseState of the tree
     */
    private ParseState completeSentenceParse() {
        List<ParseState> parseStates = new ArrayList<>();

        parseStates.add(new ShiftedState(states.get(6), new Token("b")));


        parseStates.add(new ReducedState(states.get(2), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(0)
                                                                                                    })));      

        parseStates.add(new ShiftedState(states.get(4), new Token("a")));
        parseStates.add(new ShiftedState(states.get(4), new Token("a")));
        parseStates.add(new ShiftedState(states.get(4), new Token("a")));
        parseStates.add(new ShiftedState(states.get(6), new Token("b")));

        
        parseStates.add(new ReducedState(states.get(5), getRule(2), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(5)
                                                                                                    })));
        
        parseStates.add(new ReducedState(states.get(5), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(4),
                                                                                                        parseStates.get(6)
                                                                                                    })));
        
        parseStates.add(new ReducedState(states.get(5), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(3),
                                                                                                        parseStates.get(7)
                                                                                                    })));
        
        parseStates.add(new ReducedState(states.get(3), getRule(1), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(2),
                                                                                                        parseStates.get(8)
                                                                                                    })));
        
        
        parseStates.add(new ReducedState(states.get(1), getRule(0), Arrays.asList(new ParseState[] {
                                                                                                        parseStates.get(1),
                                                                                                        parseStates.get(9)
                                                                                                    })));

        return parseStates.get(parseStates.size() - 1);
    }
    
    private ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }
}
