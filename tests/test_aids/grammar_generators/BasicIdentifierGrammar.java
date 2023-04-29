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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpStates'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpGotoTable'");
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