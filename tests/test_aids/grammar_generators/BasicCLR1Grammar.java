package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

public class BasicCLR1Grammar extends Grammar implements CLR1TestGrammar {

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
        nonTerminals.add(new NonTerminal("X"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("S"), 
            new LexicalElement[] {
                new NonTerminal("X"),
                new NonTerminal("X")
            }
        ));

        productionRules.add(new ProductionRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("X")
            }
        ));

        productionRules.add(new ProductionRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("b")
            }
        ));
    }

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
        setUpCLR1States(states, extraRootRule);
    }


    @Override
    public Map<State, Map<Token, Action>> getCLR1ActionTable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCLR1ActionTable'");
    }

    @Override
    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGotoTable'");
    }

    @Override
    public ParseState getParseRoot(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getParseRoot'");
    }

    @Override
    public Set<State> getCLR1States() {
        return new HashSet<>(getStates());
    }

    @Override
    public void setUpCLR1States(List<State> states, ProductionRule extraRootRule) {
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
    protected void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap) {
        throw new UnsupportedOperationException("Unimplemented method 'setUpGenerationBookends'");
    }

    @Override
    protected void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {
        throw new UnsupportedOperationException("Unimplemented method 'setUpRuleConvertors'");
    }

    @Override
    protected void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations) {
        throw new UnsupportedOperationException("Unimplemented method 'setUpCodeGenerations'");
    }

}
