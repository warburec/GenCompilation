package component_construction.builders.grammar_objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import code_generation.Generator;
import component_construction.ParameterError;
import grammar_objects.*;
import grammar_objects.RuleConvertor.IncompleteConversionsException;
import helper_objects.NullableTuple;

public class RuleConvertorBuilderTests {

    @Test
    public void standardUsage() {
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


        RuleConvertor actual = new RuleConvertorBuilder(grammar)
            .setConversions(conversions)
            .setBookends(bookends)
            .produceConvertor();
        
        
        RuleConvertor expected = new RuleConvertor(grammar, conversions, bookends);

        assertEquals(expected, actual);
    }

    @Test
    public void noGrammar() {
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
        
        Map<ProductionRule, Generator> conversions = new HashMap<>();
        conversions.put(rules.get(0), (elements) -> "A");
        conversions.put(rules.get(1), (elements) -> "B");

        NullableTuple<String, String> bookends = new NullableTuple<>("start", "end");


        assertThrows(ParameterError.class, () ->new RuleConvertorBuilder(null)
            .setConversions(conversions)
            .setBookends(bookends)
            .produceConvertor()
        );
    }

    @Test
    public void noConversions() {
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


        assertThrows(IncompleteConversionsException.class, () -> new RuleConvertorBuilder(grammar)
            .setBookends(bookends)
            .produceConvertor()
        );
    }

    @Test
    public void noBookends() {
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


        RuleConvertor actual = new RuleConvertorBuilder(grammar)
            .setConversions(conversions)
            .produceConvertor();
        
        
        RuleConvertor expected = new RuleConvertor(grammar, conversions);

        assertEquals(expected, actual);
    }
    
}
