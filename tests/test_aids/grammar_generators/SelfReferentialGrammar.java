package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

/**
 * H → h A
 * A → a L
 * L → l L //Self-referential
 * L → o
 */
public class SelfReferentialGrammar extends LR0TestGrammar implements SLR1TestGrammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("h"));
        tokens.add(new Token("a"));
        tokens.add(new Token("l"));
        tokens.add(new Token("o"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("H");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("H"));
        nonTerminals.add(new NonTerminal("A"));
        nonTerminals.add(new NonTerminal("L"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(
            new ProductionRule(
                new NonTerminal("H"),
                new LexicalElement[] {
                    new Token("h"),
                    new NonTerminal("A")
                }));
        
        productionRules.add(
            new ProductionRule(
                new NonTerminal("A"),
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("L")
                }));
        
        productionRules.add(
            new ProductionRule(
                new NonTerminal("L"),
                new LexicalElement[] {
                    new Token("l"),
                    new NonTerminal("L")
                }));
        
        productionRules.add(
            new ProductionRule(
                new NonTerminal("L"),
                new LexicalElement[] {
                    new Token("o")
                }));
    }

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
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

    @Override
    protected void setUpActionTable(Map<State, Map<Token, Action>> actionTable, Token endOfFile) {
        List<Token> allTokens = new ArrayList<>();
        allTokens.addAll(tokens);
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

    @Override
    protected void setUpGotoTable(Map<State, Map<NonTerminal, State>> gotoTable) {
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
    public ParseState getParseRoot(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'getParseRoot'");
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

        return actionTable;
    }
    
}
