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
public class SelfReferentialGrammar extends TestGrammar {

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
    protected void setUpStates(List<NoLookaheadState> states, ProductionRule extraRootRule) {
        states.add(new NoLookaheadState( //0
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(0), 0)
            }),
            null
        ));

        states.add(new NoLookaheadState( //1
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
            }),
            getState(0)
        ));

        states.add(new NoLookaheadState( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1),
                new GrammarPosition(getRule(1), 0)
            }),
            getState(0)
        ));

        states.add(new NoLookaheadState( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 2),
            }),
            getState(2)
        ));

        states.add(new NoLookaheadState( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0)
            }),
            getState(2)
        ));

        states.add(new NoLookaheadState( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2)
            }),
            getState(4)
        ));

        states.add(new NoLookaheadState( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
                new GrammarPosition(getRule(2), 0),
                new GrammarPosition(getRule(3), 0)
            }),
            getState(4)
        ));

        states.add(new NoLookaheadState( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2)
            }),
            getState(6)
        ));

        states.add(new NoLookaheadState( //8
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
    protected void setUpActionTable(Map<NoLookaheadState, Action> actionTable) {
        Map<Token, NoLookaheadState> currentStateActions = new HashMap<>();

        currentStateActions.put(new Token("h"), getState(2));
        actionTable.put(getState(0), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        //Accept EOF at state 1

        currentStateActions.put(new Token("a"), getState(4));
        actionTable.put(getState(2), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(3), new ReduceAction(getRule(0)));

        currentStateActions.put(new Token("l"), getState(6));
        currentStateActions.put(new Token("o"), getState(8));
        actionTable.put(getState(4), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(5), new ReduceAction(getRule(1)));

        currentStateActions.put(new Token("l"), getState(6));
        currentStateActions.put(new Token("o"), getState(8));
        actionTable.put(getState(6), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(7), new ReduceAction(getRule(2)));

        actionTable.put(getState(8), new ReduceAction(getRule(3)));
    }

    @Override
    protected void setUpGotoTable(Map<NoLookaheadState, Map<NonTerminal, NoLookaheadState>> gotoTable) {
        Map<NonTerminal, NoLookaheadState> currentGotoActions = new HashMap<>();

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
    
}
