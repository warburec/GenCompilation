package tests.testAids.GrammarGenerators;

import java.util.List;
import java.util.Map;

import code_generation.Generator;
import grammar_objects.NonTerminal;
import grammar_objects.ProductionRule;
import grammar_objects.Token;
import syntax_analysis.grammar_structure_creation.Action;
import syntax_analysis.grammar_structure_creation.State;
import syntax_analysis.parsing.ParseState;

public class IntegerCompGrammar extends TestGrammar {

    @Override
    protected void setUpTokens(List<Token> tokens) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpTokens'");
    }

    @Override
    protected NonTerminal setUpSentinal() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpSentinal'");
    }

    @Override
    protected void setUpNonTerminals(List<NonTerminal> nonTerminals) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpNonTerminals'");
    }

    @Override
    protected void setUpProductionRules(List<ProductionRule> productionRules) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpProductionRules'");
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
