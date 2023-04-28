package tests.test_aids.grammar_generators;

import java.util.List;
import java.util.Map;

import code_generation.Generator;
import grammar_objects.Identifier;
import grammar_objects.LexicalElement;
import grammar_objects.Literal;
import grammar_objects.NonTerminal;
import grammar_objects.ProductionRule;
import grammar_objects.Token;
import syntax_analysis.grammar_structure_creation.Action;
import syntax_analysis.grammar_structure_creation.State;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpActionTable'");
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