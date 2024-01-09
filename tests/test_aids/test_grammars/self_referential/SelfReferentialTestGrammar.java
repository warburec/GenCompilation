package tests.test_aids.test_grammars.self_referential;

import java.util.*;

import grammar_objects.*;
import grammars.self_referential.SelfReferentialGrammar;
import syntax_analysis.grammar_structure_creation.*;
import tests.test_aids.*;
import tests.test_aids.test_grammars.UnsupportedGrammarException;

/**
 * H → h A
 * A → a L
 * L → l L //Self-referential
 * L → o
 */
public class SelfReferentialTestGrammar extends TestGrammar {

    public SelfReferentialTestGrammar(GrammarType type) {
        super(type);
    }

    @Override
    protected Grammar setUpGrammar(GrammarType type) {
        return new SelfReferentialGrammar();
    }

    @Override
    protected void setUpStates(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
        switch (type) {
            case LR0 -> lr0States(type, states, extraRootRule, endOfFile);
            case SLR1 -> lr0States(type, states, extraRootRule, endOfFile);
            case CLR1 -> clr1States(type, states, extraRootRule, endOfFile);
            
            default -> throw new UnsupportedGrammarException(type);
        }
    }

    private void lr0States(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
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
            getState(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1),
                new GrammarPosition(getRule(1), 0)
            }),
            getState(0)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 2),
            }),
            getState(2)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0)
            }),
            getState(2)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2)
            }),
            getState(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0)
            }),
            getState(4)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2)
            }),
            getState(6)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1)
            }),
            getState(6)
        ));

        //Tree branches
        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("H")))
            .addBranch(new Route(getState(2), new Token("h")));
        
        getState(2)
            .addBranch(new Route(getState(3), new NonTerminal("A")))
            .addBranch(new Route(getState(4), new Token("a")));
        
        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("L")))
            .addBranch(new Route(getState(6), new Token("l")));
        
        getState(6)
            .addBranch(new Route(getState(7), new NonTerminal("L")))
            .addBranch(new Route(getState(8), new Token("o")));
        
        //Graph branches, links to existing states
        getState(4)
            .addBranch(new Route(getState(8), new Token("o")));
        
        getState(6)
            .addBranch(new Route(getState(6), new Token("l")));
    }

    private void clr1States(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile) {
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
            getState(0)
        ));

        states.add(new State( //2
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(0), 1, Set.of(new EOF())),
                new CLR1Position(getRule(1), 0, Set.of(new EOF()))
            }),
            getState(0)
        ));

        states.add(new State( //3
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(0), 2, Set.of(new EOF())),
            }),
            getState(2)
        ));

        states.add(new State( //4
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(1), 1, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
                new CLR1Position(getRule(3), 0, Set.of(new EOF()))
            }),
            getState(2)
        ));

        states.add(new State( //5
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(1), 2, Set.of(new EOF()))
            }),
            getState(4)
        ));

        states.add(new State( //6
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(2), 1, Set.of(new EOF())),
                new CLR1Position(getRule(2), 0, Set.of(new EOF())),
                new CLR1Position(getRule(3), 0, Set.of(new EOF()))
            }),
            getState(4)
        ));

        states.add(new State( //7
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(2), 2, Set.of(new EOF()))
            }),
            getState(6)
        ));

        states.add(new State( //8
            Set.of(new CLR1Position[] {
                new CLR1Position(getRule(3), 1, Set.of(new EOF()))
            }),
            getState(6)
        ));

        //Tree branches
        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("H")))
            .addBranch(new Route(getState(2), new Token("h")));
        
        getState(2)
            .addBranch(new Route(getState(3), new NonTerminal("A")))
            .addBranch(new Route(getState(4), new Token("a")));
        
        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("L")))
            .addBranch(new Route(getState(6), new Token("l")));
        
        getState(6)
            .addBranch(new Route(getState(7), new NonTerminal("L")))
            .addBranch(new Route(getState(8), new Token("o")));
        
        //Graph branches, links to existing states
        getState(4)
            .addBranch(new Route(getState(8), new Token("o")));
        
        getState(6)
            .addBranch(new Route(getState(6), new Token("l")));
    }

    @Override
    protected void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        switch (type) {
            case LR0 -> lr0ActionTable(type, actionTable, endOfFile);
            case SLR1 -> slr1ActionTable(type, actionTable, endOfFile);
            case CLR1 -> { /* Unimplemented */ }
            
            default -> throw new UnsupportedGrammarException(type);
        }
    }

    private void lr0ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        List<Token> allTokens = new ArrayList<>();
        allTokens.addAll(grammar.getParts().tokens());
        allTokens.add(endOfFile);

        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Token("h"), new Shift(getState(2)));

        //Accept EOF at state 1
        stateActions = actionTable.get(getState(1));
        stateActions.put(endOfFile, new Accept());

        stateActions = actionTable.get(getState(2));
        stateActions.put(new Token("a"), new Shift(getState(4)));

        stateActions = actionTable.get(getState(3));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(0)));
        }

        stateActions = actionTable.get(getState(4));
        stateActions.put(new Token("l"), new Shift(getState(6)));
        stateActions.put(new Token("o"), new Shift(getState(8)));

        stateActions = actionTable.get(getState(5));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(1)));
        }

        stateActions = actionTable.get(getState(6));
        stateActions.put(new Token("l"), new Shift(getState(6)));
        stateActions.put(new Token("o"), new Shift(getState(8)));

        stateActions = actionTable.get(getState(7));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(2)));
        }

        stateActions = actionTable.get(getState(8));
        for(Token token : allTokens) {
            stateActions.put(token, new Reduction(getRule(3)));
        }
    }

    private void slr1ActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        Map<Token, Action> stateActions = actionTable.get(getState(0));
        stateActions.put(new Token("h"), new Shift(getState(2)));

        //Accept EOF at state 1
        stateActions = actionTable.get(getState(1));
        stateActions.put(new EOF(), new Accept());

        stateActions = actionTable.get(getState(2));
        stateActions.put(new Token("a"), new Shift(getState(4)));

        stateActions = actionTable.get(getState(3));
        stateActions.put(new EOF(), new Reduction(getRule(0)));

        stateActions = actionTable.get(getState(4));
        stateActions.put(new Token("l"), new Shift(getState(6)));
        stateActions.put(new Token("o"), new Shift(getState(8)));

        stateActions = actionTable.get(getState(5));
        stateActions.put(new EOF(), new Reduction(getRule(1)));

        stateActions = actionTable.get(getState(6));
        stateActions.put(new Token("l"), new Shift(getState(6)));
        stateActions.put(new Token("o"), new Shift(getState(8)));

        stateActions = actionTable.get(getState(7));
        stateActions.put(new EOF(), new Reduction(getRule(2)));

        stateActions = actionTable.get(getState(8));
        stateActions.put(new EOF(), new Reduction(getRule(3)));
    }

    @Override
    protected void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable) {
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();

        currentGotoActions.put(new NonTerminal("H"), getState(1));
        gotoTable.put(getState(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("A"), getState(3));
        gotoTable.put(getState(2), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("L"), getState(5));
        gotoTable.put(getState(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("L"), getState(7));
        gotoTable.put(getState(6), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();
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
