package tests.test_aids.test_grammars.basic_CLR1;

import java.util.*;

import grammar_objects.*;
import grammars.basic_CLR1.BasicCLR1Grammar;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.*;

public class BasicCLR1TestGrammar extends TestGrammar {

    public BasicCLR1TestGrammar(GrammarType type) {
        super(type);
    }

    @Override
    protected Grammar setUpGrammar(GrammarType type) {
        return new BasicCLR1Grammar();
    }

    @Override
    protected void setUpStates(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
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
            getState(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(0), 1, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
            }),
            getState(0)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(0), 2, Set.of(new EOF())),
            }),
            getState(2)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 1, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
            }),
            getState(2)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 2, Set.of(new EOF())),
            }),
            getState(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(2), 1, Set.of(new EOF())),
            }),
            getState(4)
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
            getState(0)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(1), 2, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
            }),
            getState(7)
        ));

        states.add(new State( //9
            Set.of(new GrammarPosition[] {
                new CLR1Position(getRule(2), 1, Set.of(
                    new Token("a"),
                    new Token("b")
                )),
            }),
            getState(7)
        ));


        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("S")))
            .addBranch(new Route(getState(2), new NonTerminal("X")))
            .addBranch(new Route(getState(7), new Token("a")))
            .addBranch(new Route(getState(9), new Token("b")));

        getState(2)
            .addBranch(new Route(getState(3), new NonTerminal("X")))
            .addBranch(new Route(getState(4), new Token("a")))
            .addBranch(new Route(getState(6), new Token("b")));

        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("X")))
            .addBranch(new Route(getState(4), new Token("a")))
            .addBranch(new Route(getState(6), new Token("b")));
        
        getState(7)
            .addBranch(new Route(getState(8), new NonTerminal("X")))
            .addBranch(new Route(getState(7), new Token("a")))
            .addBranch(new Route(getState(9), new Token("b")));
    }

    @Override
    protected void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        // Unimplemented
    }

    @Override
    protected void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable) {
        // Unimplemented
    }

    @Override
    protected void setUpParseTrees(Map<String, ParseTreeBuilder> parseRootMap) {
        // Unimplemented
    }

    @Override
    protected void setUpRuleConvertors(GrammarType type, Map<String, Map<String, RuleConvertor>> ruleConvertorMap) {
        // Unimplemented
    }
    
    @Override
    protected void setUpCodeGenerations(GrammarType type, Map<String, Map<String, String>> codeGenerations) {
        // Unimplemented
    }

}
