package test_aids.test_grammars;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import grammar_objects.*;
import grammars.self_referential.SelfReferentialGrammar;
import syntax_analysis.grammar_structure_creation.*;
import test_aids.*;
import test_aids.TestGrammarBuilder.*;
import test_aids.test_grammars.exceptions.UnsupportedGrammarException;

/**
 * H → h A
 * A → a L
 * L → l L //Self-referential
 * L → o
 */
public class SelfReferentialTestGrammar {

    private Grammar grammar = SelfReferentialGrammar.produce();
    private EOF eof;
    private ProductionRule extraRootRule;
    private List<State> states;
    private List<Token> allTokens;

    public TestGrammar getGrammar(GrammarType grammarType) {
        TestGrammarBuilder builder = new TestGrammarBuilder(grammar);

        eof = builder.endOfFile;
        extraRootRule = builder.extraRootRule;

        allTokens = new ArrayList<>();
        allTokens.addAll(grammar.getParts().tokens());
        allTokens.add(eof);

        states = setUpStates(grammarType, builder.extraRootRule);

        TableGatherer tableGatherer = builder.setUp()
        .addStates(states)
        //Branches are already linked
        .commitStates();

        tableGatherer = setUpActionTable(grammarType, tableGatherer);

        return tableGatherer
        .selectState(states.get(0))
            .addGoto(new NonTerminal("H"), states.get(1))
            .deselectState()

        .selectState(states.get(2))
            .addGoto(new NonTerminal("A"), states.get(3))
            .deselectState()

        .selectState(states.get(4))
            .addGoto(new NonTerminal("L"), states.get(5))
            .deselectState()

        .selectState(states.get(6))
            .addGoto(new NonTerminal("L"), states.get(7))
            .deselectState()

        .commitTables()

        .commitParseTrees()
        .commitRuleConvertors()
        .commitCodeGenerations()
        .generateTestGrammar();

    }

    /**
     * Creates states and links necessary branches
     * @param grammarType
     * @param extraRootRule
     * @return
     */
    private List<State> setUpStates(GrammarType grammarType, ProductionRule extraRootRule) {
        List<State> states = new ArrayList<>();

        switch (grammarType) {
            case LR0, SLR1 -> lr0States(states);
            case CLR1 -> clr1States(states);
            
            default -> throw new UnsupportedGrammarException(grammarType);
        }

        return states;
    }

    private void lr0States(List<State> states) {
        states.add(new State( //0
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(0), 0)
            }),
            null
        ));

        states.add(new State( //1
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
            }),
            states.get(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1),
                new GrammarPosition(getRule(1), 0)
            }),
            states.get(0)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 2),
            }),
            states.get(2)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0)
            }),
            states.get(2)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2)
            }),
            states.get(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0)
            }),
            states.get(4)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2)
            }),
            states.get(6)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1)
            }),
            states.get(6)
        ));

        //Tree branches
        states.get(0)
            .addBranch(new Route(states.get(1), new NonTerminal("H")))
            .addBranch(new Route(states.get(2), new Token("h")));
        
        states.get(2)
            .addBranch(new Route(states.get(3), new NonTerminal("A")))
            .addBranch(new Route(states.get(4), new Token("a")));
        
        states.get(4)
            .addBranch(new Route(states.get(5), new NonTerminal("L")))
            .addBranch(new Route(states.get(6), new Token("l")));
        
        states.get(6)
            .addBranch(new Route(states.get(7), new NonTerminal("L")))
            .addBranch(new Route(states.get(8), new Token("o")));
        
        //Graph branches, links to existing states
        states.get(4)
            .addBranch(new Route(states.get(8), new Token("o")));
        
        states.get(6)
            .addBranch(new Route(states.get(6), new Token("l")));
    }

    private void clr1States(List<State> states) {
        states.add(new State( //0
            Set.of(new CLR1Position[] {
                new CLR1Position(extraRootRule, 0, Set.of(new EOF())),
                new CLR1Position(getRule(0), 0, Set.of(new EOF()))
            }),
            null
        ));

        states.add(new State( //1
            Set.of(new CLR1Position[] {
                new CLR1Position(extraRootRule, 1, Set.of(new EOF())),
            }),
            states.get(0)
        ));

        states.add(new State( //2
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(0), 1, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(new EOF()))
            }),
            states.get(0)
        ));

        states.add(new State( //3
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(0), 2, Set.of(new EOF())),
            }),
            states.get(2)
        ));

        states.add(new State( //4
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(1), 1, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
                new CLR1Position(getRule(3), 0, Set.of(new EOF()))
            }),
            states.get(2)
        ));

        states.add(new State( //5
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(1), 2, Set.of(new EOF()))
            }),
            states.get(4)
        ));

        states.add(new State( //6
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(2), 1, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
                new CLR1Position(getRule(3), 0, Set.of(new EOF()))
            }),
            states.get(4)
        ));

        states.add(new State( //7
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(2), 2, Set.of(new EOF()))
            }),
            states.get(6)
        ));

        states.add(new State( //8
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(3), 1, Set.of(new EOF()))
            }),
            states.get(6)
        ));

        //Tree branches
        states.get(0)
            .addBranch(new Route(states.get(1), new NonTerminal("H")))
            .addBranch(new Route(states.get(2), new Token("h")));
        
        states.get(2)
            .addBranch(new Route(states.get(3), new NonTerminal("A")))
            .addBranch(new Route(states.get(4), new Token("a")));
        
        states.get(4)
            .addBranch(new Route(states.get(5), new NonTerminal("L")))
            .addBranch(new Route(states.get(6), new Token("l")));
        
        states.get(6)
            .addBranch(new Route(states.get(7), new NonTerminal("L")))
            .addBranch(new Route(states.get(8), new Token("o")));
        
        //Graph branches, links to existing states
        states.get(4)
            .addBranch(new Route(states.get(8), new Token("o")));
        
        states.get(6)
            .addBranch(new Route(states.get(6), new Token("l")));
    }

    protected TableGatherer setUpActionTable(GrammarType type, TableGatherer tableGatherer) {
        switch (type) {
            case LR0 -> lr0ActionTable(tableGatherer);
            case SLR1 -> slr1ActionTable(tableGatherer);
            case CLR1 -> { /* Unimplemented */ }
            
            default -> throw new UnsupportedGrammarException(type);
        }

        return tableGatherer;
    }

    private void lr0ActionTable(TableGatherer tableGatherer) {
        tableGatherer
        .selectState(states.get(0))
            .addAction(new Token("h"), new Shift(states.get(2)))
            .deselectState()

        //Accept EOF at state 1
        .selectState(states.get(1))
            .addAction(eof, new Accept())
            .deselectState()

        .selectState(states.get(2))
            .addAction(new Token("a"), new Shift(states.get(4)))
            .deselectState()

        .selectState(states.get(3))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(0))
            )))
            .deselectState()

        .selectState(states.get(4))
            .addAction(new Token("l"), new Shift(states.get(6)))
            .addAction(new Token("o"), new Shift(states.get(8)))
            .deselectState()

        .selectState(states.get(5))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(1))
            )))
            .deselectState()

        .selectState(states.get(6))
            .addAction(new Token("l"), new Shift(states.get(6)))
            .addAction(new Token("o"), new Shift(states.get(8)))
            .deselectState()

        .selectState(states.get(7))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(2))
            )))
            .deselectState()

        .selectState(states.get(8))
            .addActions(allTokens.stream().collect(Collectors.toMap(
                Function.identity(), 
                token -> new Reduction(getRule(3))
            )))
            .deselectState();
    }

    private void slr1ActionTable(TableGatherer tableGatherer) {
        tableGatherer
        .selectState(states.get(0))
            .addAction(new Token("h"), new Shift(states.get(2)))
            .deselectState()

        //Accept EOF at state 1
        .selectState(states.get(1))
            .addAction(new EOF(), new Accept())
            .deselectState()

        .selectState(states.get(2))
            .addAction(new Token("a"), new Shift(states.get(4)))
            .deselectState()

        .selectState(states.get(3))
            .addAction(new EOF(), new Reduction(getRule(0)))
            .deselectState()

        .selectState(states.get(4))
            .addAction(new Token("l"), new Shift(states.get(6)))
            .addAction(new Token("o"), new Shift(states.get(8)))
            .deselectState()

        .selectState(states.get(5))
            .addAction(new EOF(), new Reduction(getRule(1)))
            .deselectState()

        .selectState(states.get(6))
            .addAction(new Token("l"), new Shift(states.get(6)))
            .addAction(new Token("o"), new Shift(states.get(8)))
            .deselectState()

        .selectState(states.get(7))
            .addAction(new EOF(), new Reduction(getRule(2)))
            .deselectState()

        .selectState(states.get(8))
            .addAction(new EOF(), new Reduction(getRule(3)))
            .deselectState();
    }
    
    private ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }

}
