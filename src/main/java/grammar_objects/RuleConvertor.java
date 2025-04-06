package grammar_objects;

import java.util.*;

import code_generation.Generator;
import helper_objects.NullableTuple;

//TODO: Test
/**
 * Defines conversions for the rules within the specified grammar.
 */
public record RuleConvertor(
    Grammar grammar,
    Map<ProductionRule, Generator> conversions,
    NullableTuple<String, String> bookends
) {
    
    public static final ProductionRule ROOT_RULE = null;

    /**
     * Defines conversions for the rules within the specified grammar
     * @param grammar The grammar for conversions
     * @param conversions The conversions for every grammar rule
     * @param bookends Any constant strings which will bookend produced generations. May be null
     */
    public RuleConvertor(
        Grammar grammar,
        Map<ProductionRule, Generator> conversions,
        NullableTuple<String, String> bookends
    ) {
        if (grammar == null) throw new RuntimeException("A grammar must be defined");
        if (conversions == null) throw new RuntimeException("Production rule conversions must be defined");

        if(bookends == null)
            bookends = new NullableTuple<String,String>("", "");

        if (!conversions.containsKey(ROOT_RULE))
            conversions.put(ROOT_RULE, (elements) -> elements[0].getGeneration());

        //TODO: Throw error if num of conversions are not the same as grammer rules including the root rule

        this.grammar = grammar;
        this.bookends = bookends;
        this.conversions = conversions;
    }

    /**
     * Defines conversions for the rules within the specified grammar
     * @param grammar The grammar for conversions
     * @param conversions The conversions for every grammar rule
     */
    public RuleConvertor(
        Grammar grammar,
        Map<ProductionRule, Generator> conversions
    ){
        this(grammar, conversions, null);
    }

}
