package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.*;

public abstract class TestGrammar extends Grammar {
    private List<NoLookaheadState> getState = new ArrayList<>();
    private Map<String, Map<String, String>> codeGenerations = new HashMap<>();                          //Language, <Sentence, Code>
    private Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap = new HashMap<>(); //Language, <Sentence, ruleConverterMap>
    private Map<String, Map<String, String[]>> generationBookendMap = new HashMap<>();                   //Language, <Sentence, {preGeneration, postGeneration}>

    private Map<NoLookaheadState, Action> actionTable = new HashMap<>();
    private Map<NoLookaheadState, Map<NonTerminal, NoLookaheadState>> gotoTable = new HashMap<>();

    public TestGrammar() {
        setUpTokens(tokens);
        sentinal = setUpSentinal();
        setUpNonTerminals(nonTerminals);
        setUpProductionRules(productionRules);

        setUpStates(getState, new ProductionRule(null, new LexicalElement[] {sentinal}));
        setUpActionTable(actionTable);
        setUpGotoTable(gotoTable);

        setUpRuleConvertors(ruleConvertorMap);
        setUpCodeGenerations(codeGenerations);
        setUpGenerationBookends(generationBookendMap);
    }


    protected abstract void setUpTokens(List<Token> tokens);
    protected abstract NonTerminal setUpSentinal();
    protected abstract void setUpNonTerminals(List<NonTerminal> nonTerminals);
    

    protected abstract void setUpProductionRules(List<ProductionRule> productionRules);
    
    public ProductionRule getRule(int index) {
        return productionRules.get(index);
    }


    protected abstract void setUpStates(List<NoLookaheadState> states, ProductionRule extraRootRule);

    public Set<NoLookaheadState> getGetState() {
        return new HashSet<>(getState);
    }

    protected NoLookaheadState getState(int index) {
        return getState.get(index);
    }


    protected abstract void setUpActionTable(Map<NoLookaheadState, Action> actionTable);

    public Map<NoLookaheadState, Action> getActionTable() {
        return actionTable;
    }

    protected abstract void setUpGotoTable(Map<NoLookaheadState, Map<NonTerminal, NoLookaheadState>> gotoTable);

    public Map<NoLookaheadState, Map<NonTerminal, NoLookaheadState>> getGotoTable() {
        return gotoTable;
    }


    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public abstract ParseState getParseRoot(String sentence);


    protected abstract void setUpGenerationBookends(Map<String, Map<String, String[]>> generationBookendMap);

    public String[] getGenerationBookends(String sentence, String language) {
        String[] bookends = null;

        try {
            bookends = generationBookendMap.get(language).get(sentence);
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and generation bookends", sentence);
        }

        if(bookends == null) {
            throw new UnsupportedSentenceException("generation bookends", sentence);
        }

        return bookends;
    }


    protected abstract void setUpRuleConvertors(Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap);

    public Map<ProductionRule, Generator> getRuleConvertor(String sentence, String language) {
        Map<ProductionRule, Generator> ruleConvertor = null;

        try {
            ruleConvertor = ruleConvertorMap.get(language).get(sentence);
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and rule convertor", sentence);
        }

        if(ruleConvertor == null) {
            throw new UnsupportedSentenceException("rule convertor", sentence);
        }

        return ruleConvertor;
    }


    protected abstract void setUpCodeGenerations(Map<String, Map<String, String>> codeGenerations);

    public String getGeneratedCode(String sentence, String language) {
        String generatedCode = null;

        try {
            generatedCode = codeGenerations.get(language).get(sentence);
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and code generation", sentence);
        }

        if(generatedCode == null) {
            throw new UnsupportedSentenceException("code generation", sentence);
        }

        return generatedCode;
    }
}