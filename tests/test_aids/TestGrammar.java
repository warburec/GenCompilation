package tests.test_aids;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import helperObjects.NullableTuple;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.test_grammars.UnsupportedSentenceException;

public abstract class TestGrammar { //TODO allow implementation of grammar interface for propper testing
    
    protected Grammar grammar;

    //TODO: Change all based on chosen grammar type, rework type usage
    private List<State> states = new ArrayList<>();
    private Map<State, Map<Token, Action>> actionTable = new HashMap<>();
    private Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();
    private Map<String, Map<String, RuleConvertor>> ruleConvertorMap = new HashMap<>(); //Language, <Sentence, RuleConvertor>
    private Map<String, Map<String, String>> codeGenerations = new HashMap<>();         //Language, <Sentence, Code>
    
    public TestGrammar(GrammarType type) {
        grammar = setUpGrammar(type);

        setUpStates(
            type,
            states, 
            new ProductionRule(null, new LexicalElement[] {grammar.getParts().sentinal()}),
            new EOF()
        );

        for (State state : getStates()) {
            actionTable.put(state, new HashMap<>());
            gotoTable.put(state, new HashMap<>());
        }

        setUpActionTable(type, actionTable, new EOF());
        setUpGotoTable(type, gotoTable);

        setUpRuleConvertors(type, ruleConvertorMap);
        setUpCodeGenerations(type, codeGenerations);
    }

    protected abstract Grammar setUpGrammar(GrammarType type);

    protected abstract void setUpStates(GrammarType type, List<State> states, ProductionRule extraRootRule, Token endOfFile);
    /**
     * Populates the action table for this grammar, states are populated in-order by setUpStates() and may be accessed with getState(int index)
     * @param type The type of the grammar to be used
     * @param actionTable The action table to be populated
     * @param endOfFile The end of file token
     */
    protected abstract void setUpActionTable(GrammarType type, Map<State, Map<Token, Action>> actionTable, Token endOfFile);
    /**
     * Populates the goto table for this grammar, states are populated in-order by setUpStates() and may be accessed with getState(int index)
     * @param type The type of the grammar to be used
     * @param gotoTable The goto table to be populated
     */
    protected abstract void setUpGotoTable(GrammarType type, Map<State, Map<NonTerminal, State>> gotoTable);
    protected abstract void setUpRuleConvertors(GrammarType type, Map<String, Map<String, RuleConvertor>> ruleConvertorMap);
    protected abstract void setUpCodeGenerations(GrammarType type, Map<String, Map<String, String>> codeGenerations);

    public ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }

    public GrammarParts getParts() {
        return grammar.getParts();
    }

    public Set<State> getStates() {
        return new HashSet<>(states);
    }

    /**
     * Gets a state populated in-order by setUpStates()
     * @param index The index of the state to be returned
     * @return The state found
     */
    protected State getState(int index) {
        return states.get(index);
    }

    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public abstract ParseState getParseRoot(String sentence); //TODO: Consider reworking

    public Map<State, Map<Token, Action>> getActionTable() {
        return actionTable;
    }

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        return gotoTable;
    }

    public NullableTuple<String, String> getGenerationBookends(String sentence, String language) {
        NullableTuple<String, String> bookends = null;

        try {
            bookends = ruleConvertorMap.get(language).get(sentence).getBookends();
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and generation bookends", sentence);
        }

        if(bookends == null) {
            throw new UnsupportedSentenceException("generation bookends", sentence);
        }

        return bookends;
    }

    public Map<ProductionRule, Generator> getRuleConvertor(String sentence, String language) {
        Map<ProductionRule, Generator> ruleConvertor = null;

        try {
            ruleConvertor = ruleConvertorMap.get(language).get(sentence).getConversions();
        }
        catch (NullPointerException e) {
            throw new UnsupportedSentenceException("langage and rule convertor", sentence);
        }

        if(ruleConvertor == null) {
            throw new UnsupportedSentenceException("rule convertor", sentence);
        }

        return ruleConvertor;
    }

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
