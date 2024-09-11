package grammar_objects;

import java.util.*;

import code_generation.Generator;
import helper_objects.NullableTuple;

public abstract class RuleConvertor {
    public static final Integer ROOT_RULE_INDEX = null;
    public static final ProductionRule ROOT_RULE = null;

    private Grammar grammar;
    private NullableTuple<String, String> bookends;
    private Map<ProductionRule, Generator> conversions = new HashMap<>();

    public RuleConvertor() {
        grammar = setUpGrammar();
        bookends = setUpBookends();

        if(bookends == null) {
            bookends = new NullableTuple<String,String>("", "");
        }

        setUpRuleConvertors(new RuleOrganiser());
    }

    /**
     * Set up the grammar for this convertor
     * @return The grammar to be used
     */
    protected abstract Grammar setUpGrammar();

    /**
     * Set up the constant starting and ending strings for conversions
     * @return A NullableTuple of the starting string and ending string, may be null in which case the bookends will be taken as ("","")
     */
    protected abstract NullableTuple<String, String> setUpBookends();

    /**
     * Sets up code conversions for each production rule, for when the rules are sanctioned
     * @param ruleOrganiser A helper object for conversion setup
     */
    protected abstract void setUpRuleConvertors(RuleOrganiser ruleOrganiser);

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

    protected class RuleOrganiser {

        /**
         * Sets the conversion code to run when the specified rule is sanctioned 
         * @param ruleNumber The rule index. ROOT_RULE_INDEX, to run code after the final rule is sanctioned (useful for formatting).
         * @param generatorFunction The function to be run when the specified rule is sanctioned.
         * @return The original organiser object to allof method chaining.
         */
        public RuleOrganiser setConversion(Integer ruleNumber, Generator generatorFunction) {
            if(ruleNumber < 0 && ruleNumber != ROOT_RULE_INDEX) { 
                throw new RuntimeException("The given rule number must be >= 0 or ROOT_RULE_INDEX");
            }

            ProductionRule rule = null;

            if(ruleNumber != ROOT_RULE_INDEX) {
                rule = grammar.getRule(ruleNumber);
            }

            conversions.put(rule, generatorFunction);

            return this;
        }

    }

}
