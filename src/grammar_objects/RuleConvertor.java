package grammar_objects;

import java.util.*;

import code_generation.Generator;
import helperObjects.NullableTuple;

public abstract class RuleConvertor {

    private Grammar grammar;
    private NullableTuple<String, String> bookends;
    private Map<ProductionRule, Generator> conversions = new HashMap<>();

    public RuleConvertor() {
        grammar = setUpGrammar();
        bookends = setUpBookends();
        setUpRuleConvertors(grammar, conversions);
    }

    /**
     * Set up the grammar for this convertor
     * @return The grammar to be used
     */
    protected abstract Grammar setUpGrammar();

    /**
     * Set up the constant starting and ending strings for conversions
     * @return A NullableTuple of the starting string and ending string
     */
    protected abstract NullableTuple<String, String> setUpBookends();

    /**
     * Populates the map of production rules to their conversion Generator
     * @param ruleConversions The map to be populated
     */
    protected abstract void setUpRuleConvertors(Grammar grammar, Map<ProductionRule, Generator> ruleConversions);

    /**
     * Gets the start and ending strings for conversions
     * @return A tuple of the starting and ending strings
     */
    public NullableTuple<String, String> getBookends() {
        return bookends;
    }

    /**
     * Gets the mapping of production rules to the Generators for their conversions
     * @return The conversion map
     */
    public Map<ProductionRule, Generator> getConversions() {
        return conversions;
    }

    /**
     * Helper function to get ProductionRule from the grammar returned from setUpGrammar()
     * @param index The index of the ProductionRule in the Gramar
     * @return The ProductionRule
     */
    protected ProductionRule getRule(int index) {
        return grammar.getRule(index);
    }
}
