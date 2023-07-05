package tests.test_aids.grammar_generators;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.State;
import tests.test_aids.GrammarParts;

public abstract class Grammar {
    
    protected List<Token> tokens = new ArrayList<>();
    protected List<NonTerminal> nonTerminals = new ArrayList<>();
    protected List<ProductionRule> productionRules = new ArrayList<>();
    protected NonTerminal sentinal;

    private List<State> states = new ArrayList<>();

    private Map<String, Map<String, String>> codeGenerations = new HashMap<>();                          //Language, <Sentence, Code>
    private Map<String, Map<String, Map<ProductionRule, Generator>>> ruleConvertorMap = new HashMap<>(); //Language, <Sentence, ruleConverterMap>
    private Map<String, Map<String, String[]>> generationBookendMap = new HashMap<>();                   //Language, <Sentence, {preGeneration, postGeneration}>
    
    public Grammar() {
        setUpTokens(tokens);
        sentinal = setUpSentinal();
        setUpNonTerminals(nonTerminals);
        setUpProductionRules(productionRules);

        setUpStates(states, new ProductionRule(null, new LexicalElement[] {sentinal}));

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

    public GrammarParts getParts() {
        return new GrammarParts(
            Set.copyOf(tokens), 
            Set.copyOf(nonTerminals), 
            Set.copyOf(productionRules), 
            sentinal
        );
    }


    protected abstract void setUpStates(List<State> states, ProductionRule extraRootRule);

    public Set<State> getStates() {
        return new HashSet<>(states);
    }

    protected State getState(int index) {
        return states.get(index);
    }

    
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
