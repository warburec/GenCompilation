package component_construction.builders.grammar_objects;

import java.util.*;
import java.util.Map.Entry;

import code_generation.Generator;
import grammar_objects.*;
import helper_objects.NullableTuple;

public class RuleConvertorBuilder {

    protected Grammar grammar;
    protected Map<ProductionRule, Generator> conversions = new HashMap<>();
    protected NullableTuple<String, String> bookends = null;
    protected Generator defaultConversion = null;

    public RuleConvertorBuilder(Grammar grammar) {
        this.grammar = grammar;
    }

    public RuleConvertor produceConvertor() {
        if (defaultConversion != null)
            populateDefaultConversions();

        return new RuleConvertor(grammar, conversions, bookends);
    }

    /**
     * Sets the constant strings which will bookend produced generations
     * @param bookends The strings to bookend produced generations
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder setBookends(NullableTuple<String, String> bookends) {
        this.bookends = bookends;
        return this;
    }

    /**
     * Sets the constant strings which will bookend produced generations
     * @param startBookend The constant starting string
     * @param endBookend The constant ending string
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder setBookends(String startBookend, String endBookend) {
        this.bookends = new NullableTuple<>(startBookend, endBookend);
        return this;
    }

    /**
     * Enables default conversions for each grammar rule
     * The default conversion will concatenate the generations of each element within each rule
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder enableDefaultConversions() {
        this.defaultConversion = (elements) -> Arrays
            .stream(elements)
            .map((element) -> element.getGeneration().toString())
            .reduce("", (subtotal, current) -> subtotal + current);
        
        return this;
    }

    /**
     * Enables default conversions for each grammar rule using the specified generator
     * @param defaultGeneratorFunction The default generator to convert each grammar rule with
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder enableDefaultConversions(Generator defaultGeneratorFunction) {
        this.defaultConversion = defaultGeneratorFunction;
        return this;
    }

    /**
     * Sets the conversion function for the additional root rule of the grammar
     * @param generatorFunction The conversion/generator function to be used
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder setRootConversion(Generator generatorFunction) {
        this.conversions.put(RuleConvertor.ROOT_RULE, generatorFunction);
        return this;
    }

    /**
     * Sets the conversion function for the specified grammar rule
     * @param rule The grammar rule the conversion will be applied for
     * @param generatorFunction The conversion/generator function to be used
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder setConversion(ProductionRule rule, Generator generatorFunction) {
        this.conversions.put(rule, generatorFunction);
        return this;
    }

    /**
     * Sets the conversion function for the specified grammar rule
     * @param ruleNumber The index of the grammar rule the conversion will be applied for
     * @param generatorFunction The conversion/generator function to be used
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder setConversion(int ruleNumber, Generator generatorFunction) {
        if(ruleNumber < 0)
            throw new RuntimeException("The given rule number must be >= 0");

        ProductionRule rule = grammar.getRule(ruleNumber);
        conversions.put(rule, generatorFunction);

        return this;
    }

    /**
     * Adds the given conversion functions for the specified grammar rule
     * @param conversions A map of grammar rules to generator functions
     * @return This builder to allow method chaining
     */
    public RuleConvertorBuilder setConversions(Map<ProductionRule, Generator> conversions) {
        for (Entry<ProductionRule, Generator> entry : conversions.entrySet()) {
            setConversion(entry.getKey(), entry.getValue());
        }
        
        return this;
    }

    /**
     * Selects the specified grammar rule
     * @param rule The grammar rule
     * @return An object for applying options for the selected grammar rule
     */
    public RuleOptions rule(ProductionRule rule) {
        return new RuleOptions(this, rule);
    }

    /**
     * Selects the specified grammar rule
     * @param ruleNumber The index of the grammar rule
     * @return An object for applying options for the selected grammar rule
     */
    public RuleOptions rule(int ruleNumber) {
        if(ruleNumber < 0)
            throw new RuntimeException("The given rule number must be >= 0");

        ProductionRule rule = grammar.getRule(ruleNumber);
        return new RuleOptions(this, rule);
    }


    private void populateDefaultConversions() {
        for (ProductionRule productionRule : grammar.productionRules()) {
            if (conversions.containsKey(productionRule)) continue;

            conversions.put(productionRule, defaultConversion);
        }
    }


    /**
     * Options that may be applied for a selected grammar rule
     */
    protected record RuleOptions(RuleConvertorBuilder builder, ProductionRule rule) {

        /**
         * Sets the conversion for the slected grammar rule
         * @param generatorFuction The conversion/generator function to be used
         * @return The rule convertor builder
         */
        public RuleConvertorBuilder setConversion(Generator generatorFuction) {
            builder.conversions.put(rule, generatorFuction);
            return builder;
        }

    }
}
