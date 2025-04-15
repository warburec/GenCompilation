package test_aids;

import java.util.*;

import grammar_objects.*;
import syntax_analysis.grammar_structure_creation.*;
import syntax_analysis.parsing.ParseState;
import test_aids.test_grammars.UnsupportedSentenceException;

//TODO: Rework to use Grammar builder

public abstract class TestGrammar { //TODO allow implementation of grammar interface for propper testing
    
    protected Grammar grammar;

    //TODO: Change all based on chosen grammar type, rework type usage
    private List<State> states = new ArrayList<>();
    private Map<State, Map<Token, Action>> actionTable = new HashMap<>();
    private Map<State, Map<NonTerminal, State>> gotoTable = new HashMap<>();
    private Map<String, Map<String, RuleConvertor>> ruleConvertorMap = new HashMap<>(); //Language, <Sentence, RuleConvertor>
    private Map<String, Map<String, String>> codeGenerations = new HashMap<>();         //Language, <Sentence, Code>
    private Map<String, ParseTreeBuilder> parseRootMap = new HashMap<>();
    
    protected interface ParseTreeBuilder {
        public ParseState buildTree();
    }

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

        setUpParseTrees(parseRootMap);

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
    /**
     * Sets up the parse trees for sentences supported by this TestGrammar
     * @param parseRootMap A map of sentence name and the builder function for its parse tree
     */
    protected abstract void setUpParseTrees(Map<String, ParseTreeBuilder> parseRootMap);
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

    public Map<State, Map<Token, Action>> getActionTable() {
        return actionTable;
    }

    public Map<State, Map<NonTerminal, State>> getGotoTable() {
        return gotoTable;
    }

    /**
     * Gets the root parse state (and contained parse tree) for a given sentence
     * @param sentence The sentence to be parsed
     * @return The root ParseState of the parse tree
     */
    public ParseState getParseRoot(String sentence) { //TODO: Make dependent on grammar type
        ParseState root = parseRootMap.get(sentence).buildTree();

        if(root == null) {
            throw new UnsupportedSentenceException("parse root", sentence);
        }

        return root;
    }

    public RuleConvertor getRuleConvertor(String sentence, String language) {
        RuleConvertor ruleConvertor = null;

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
