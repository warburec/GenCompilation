package grammar_objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

import code_generation.Generator;
import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.RuleConvertor.IncompleteConversionsException;
import helper_objects.NullableTuple;

public class RuleConvertorTests {
    
    @Test
    public void normalUsage_defaultRootRule() {
        List<ProductionRule> rules = List.of(
            new ProductionRule(
                new NonTerminal("A"), 
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("B")
                }
            ),
            new ProductionRule(
                new NonTerminal("B"), 
                new LexicalElement[] {
                    new Token("b")
                }
            )
        );

        Grammar grammar = new GrammarBuilder()
            .addRules(rules)
            .setSentinal(new NonTerminal("A"))
            .produceGrammar();
        
        Map<ProductionRule, Generator> conversions = new HashMap<>();
        conversions.put(rules.get(0), (elements) -> "A");
        conversions.put(rules.get(1), (elements) -> "B");

        NullableTuple<String, String> bookends = new NullableTuple<>("start", "end");


        RuleConvertor actual = new RuleConvertor(grammar, conversions, bookends);


        assertEquals(bookends, actual.bookends());
        assertEquals(conversions, actual.conversions());
        assertEquals(grammar, actual.grammar());
        assertEquals((Generator)(elements) -> elements[0].getGeneration(), actual.conversions().get(RuleConvertor.ROOT_RULE));
    }

    @Test
    public void normalUsage_definedRootRule() {
        List<ProductionRule> rules = List.of(
            new ProductionRule(
                new NonTerminal("A"), 
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("B")
                }
            ),
            new ProductionRule(
                new NonTerminal("B"), 
                new LexicalElement[] {
                    new Token("b")
                }
            )
        );

        Grammar grammar = new GrammarBuilder()
            .addRules(rules)
            .setSentinal(new NonTerminal("A"))
            .produceGrammar();
        
        Generator rootConvertor = (elements) -> "ROOT";
        
        Map<ProductionRule, Generator> conversions = new HashMap<>();
        conversions.put(RuleConvertor.ROOT_RULE, rootConvertor);
        conversions.put(rules.get(0), (elements) -> "A");
        conversions.put(rules.get(1), (elements) -> "B");

        NullableTuple<String, String> bookends = new NullableTuple<>("start", "end");


        RuleConvertor actual = new RuleConvertor(grammar, conversions, bookends);

        
        assertEquals(bookends, actual.bookends());
        assertEquals(conversions, actual.conversions());
        assertEquals(grammar, actual.grammar());
        assertEquals(rootConvertor, actual.conversions().get(RuleConvertor.ROOT_RULE));
    }

    @Test
    public void tooFewConversions() {
        List<ProductionRule> rules = List.of(
            new ProductionRule(
                new NonTerminal("A"), 
                new LexicalElement[] {
                    new Token("a"),
                    new NonTerminal("B")
                }
            ),
            new ProductionRule(
                new NonTerminal("B"), 
                new LexicalElement[] {
                    new Token("b")
                }
            )
        );

        Grammar grammar = new GrammarBuilder()
            .addRules(rules)
            .setSentinal(new NonTerminal("A"))
            .produceGrammar();
        
        
        Map<ProductionRule, Generator> conversions = new HashMap<>();
        conversions.put(rules.get(0), (elements) -> "A");

        NullableTuple<String, String> bookends = new NullableTuple<>("start", "end");


        //TODO: Check exception message format
        assertThrows(IncompleteConversionsException.class, () -> new RuleConvertor(grammar, conversions, bookends));
    }
}
