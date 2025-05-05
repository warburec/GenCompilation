package grammar_objects;

import java.util.*;

import code_generation.Generator;
import component_construction.ParameterError;
import helper_objects.NullableTuple;

/**
 * Defines conversions for the rules within the specified grammar.
 */
public record RuleConvertor(
    Grammar grammar,
    Map<ProductionRule, Generator> conversions,
    NullableTuple<String, String> bookends
) {
    //TODO: Consider if the sentinal token should be listed in the productionSequence
    public static final ProductionRule ROOT_RULE = new ProductionRule(null, null);

    /**
     * Defines conversions for the rules within the specified grammar
     * @param grammar The grammar for conversions
     * @param conversions The conversions for every grammar rule
     * @param bookends Any constant strings which will bookend produced generations. May be null, in which case the Generator will be {@code (elements) -> elements[0].getGeneration() }
     */
    public RuleConvertor(
        Grammar grammar,
        Map<ProductionRule, Generator> conversions,
        NullableTuple<String, String> bookends
    ) {
        if (grammar == null) throw new ParameterError("A grammar must be defined");
        if (conversions == null) throw new ParameterError("Production rule conversions must be defined");

        if (bookends == null)
            bookends = new NullableTuple<String,String>("", "");

        try {
            if (!conversions.containsKey(ROOT_RULE))
                conversions.put(ROOT_RULE, (elements) -> elements[0].getGeneration());
        }
        catch (NullPointerException e) {
            conversions = new HashMap<>();
            conversions.put(ROOT_RULE, (elements) -> elements[0].getGeneration());
            conversions.putAll(conversions);
        }

        if (conversions.size() < grammar.getProductionRules().size() + 1)
            throw new IncompleteConversionsException(conversions, grammar.getProductionRules());

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
    ) {
        this(grammar, conversions, null);
    }

    public class IncompleteConversionsException extends RuntimeException {

        protected String message;

        public IncompleteConversionsException(Map<ProductionRule, Generator> conversions, List<ProductionRule> productionRules) {
            List<ProductionRule> notImplemented = new ArrayList<>();

            for (ProductionRule productionRule : productionRules) {
                if (!conversions.containsKey(productionRule))
                    notImplemented.add(productionRule);
            }

            message = "There were no conversions for the following rules:\n";

            if (!conversions.containsKey(ROOT_RULE))
                message += "    ROOT_RULE\n";

            for (ProductionRule productionRule : notImplemented) {
                message += "    " + productionRule.toString() + "\n";
            }

            message = message.strip();
        }

        @Override
        public String getMessage() {
            return message;
        }

    }
}
