package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierGrammar extends TestGrammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        tokens.add(new Token("="));
        tokens.add(new Token("+"));
        tokens.add(new Token(";"));
        tokens.add(new Identifier("identifier")); //TODO
        tokens.add(new Literal("number"));
    }

    @Override
    protected NonTerminal setUpSentinal() {
        return new NonTerminal("statement list");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        nonTerminals.add(new NonTerminal("statement list"));
        nonTerminals.add(new NonTerminal("statement"));
        nonTerminals.add(new NonTerminal("element"));
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        productionRules.add(new ProductionRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement")
            }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement list"),
                new NonTerminal("statement")
            }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("statement"),
            new LexicalElement[] {
                new Identifier("identifier"),
                new Token("="),
                new NonTerminal("element"),
                new Token("+"),
                new NonTerminal("element"),
                new Token(";")
            }));

        productionRules.add(new ProductionRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Identifier("identifier")
            }));
        
        productionRules.add(new ProductionRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Literal("number")
            }));
    }

    @Override
    protected void setUpStates(List<State> states, ProductionRule extraRootRule) {
        states.add(new State( //0
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 0),
                new GrammarPosition(getRule(1), 0),
                new GrammarPosition(getRule(0), 0),
                new GrammarPosition(getRule(2), 0)
            }),
            null
        ));

        states.add(new State( //1
            Set.of(new GrammarPosition[] {
                new GrammarPosition(extraRootRule, 1),
                new GrammarPosition(getRule(1), 1),
                new GrammarPosition(getRule(2), 0)
            }),
            getState(0)
        ));

        states.add(new State( //2
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(1), 2),
            }),
            getState(1)
        ));

        states.add(new State( //3
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 1),
            }),
            getState(1)
        ));

        states.add(new State( //4
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 2),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            getState(3)
        ));

        states.add(new State( //5
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 3)
            }),
            getState(4)
        ));

        states.add(new State( //6
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 4),
                new GrammarPosition(getRule(3), 0),
                new GrammarPosition(getRule(4), 0)
            }),
            getState(5)
        ));

        states.add(new State( //7
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(2), 5)
            }),
            getState(6)
        ));

        states.add(new State( //8
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(3), 1)
            }),
            getState(6)
        ));

        states.add(new State( //9
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(4), 1)
            }),
            getState(6)
        ));

        states.add(new State( //10
            Set.of(new GrammarPosition[] {
                new GrammarPosition(getRule(0), 1)
            }),
            getState(0)
        ));

        //Branches
        getState(0)
            .addBranch(new Route(getState(1), new NonTerminal("statement list")))
            .addBranch(new Route(getState(10), new NonTerminal("statement")))
            .addBranch(new Route(getState(3), new Identifier("identifier")));
        
        getState(1)
            .addBranch(new Route(getState(2), new NonTerminal("statement")))
            .addBranch(new Route(getState(3), new Identifier("identifier")));
        
        getState(3)
            .addBranch(new Route(getState(4), new Token("=")));
        
        getState(4)
            .addBranch(new Route(getState(5), new NonTerminal("element")))
            .addBranch(new Route(getState(8), new Identifier("identifier")))
            .addBranch(new Route(getState(9), new Literal("number")));
        
        getState(5)
            .addBranch(new Route(getState(6), new Token("+")));
        
        getState(6)
            .addBranch(new Route(getState(7), new NonTerminal("element")))
            .addBranch(new Route(getState(8), new Identifier("identifier")))
            .addBranch(new Route(getState(9), new Literal("number")));
    }

    @Override
    protected void setUpActionTable(Map<State, Action> actionTable) {
        Map<Token, State> currentStateActions = new HashMap<>();

        currentStateActions.put(new Identifier("identifier"), getState(3));
        actionTable.put(getState(0), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Identifier("identifier"), getState(3));
        actionTable.put(getState(1), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(2), new ReduceAction(getRule(1)));

        currentStateActions.put(new Token("="), getState(4));
        actionTable.put(getState(3), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Identifier("identifier"), getState(8));
        currentStateActions.put(new Literal("number"), getState(9));
        actionTable.put(getState(4), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Token("+"), getState(6));
        actionTable.put(getState(5), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        currentStateActions.put(new Identifier("identifier"), getState(8));
        currentStateActions.put(new Literal("number"), getState(9));
        actionTable.put(getState(6), new ShiftAction(new HashMap<>(currentStateActions)));
        currentStateActions.clear();

        actionTable.put(getState(7), new ReduceAction(getRule(2)));

        actionTable.put(getState(8), new ReduceAction(getRule(3)));

        actionTable.put(getState(9), new ReduceAction(getRule(4)));

        actionTable.put(getState(10), new ReduceAction(getRule(0)));
    }

    @Override
    protected void setUpGotoTable(Map<State, Map<NonTerminal, State>> gotoTable) {
        Map<NonTerminal, State> currentGotoActions = new HashMap<>();

        currentGotoActions.put(new NonTerminal("statement list"), getState(1));
        currentGotoActions.put(new NonTerminal("statement"), getState(10));
        gotoTable.put(getState(0), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("statement"), getState(2));
        gotoTable.put(getState(1), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("element"), getState(5));
        gotoTable.put(getState(4), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();

        currentGotoActions.put(new NonTerminal("element"), getState(7));
        gotoTable.put(getState(6), new HashMap<>(currentGotoActions));
        currentGotoActions.clear();
    }

    @Override
    public ParseState getParseRoot(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getParseRoot'");
    }

    @Override
    protected void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpGenerationBookends'");
    }

    @Override
    protected void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpRuleConvertors'");
    }

    @Override
    protected void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpCodeGenerations'");
    }

}