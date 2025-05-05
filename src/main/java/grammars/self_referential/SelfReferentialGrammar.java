package grammars.self_referential;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

/**
 * H → h A
 * A → a L
 * L → l L //Self-referential
 * L → o
 */
public class SelfReferentialGrammar {

    public static Grammar produce() {
        return new GrammarBuilder()
        .setSentinal(new NonTerminal("H"))
        .addRule(
            new NonTerminal("H"),
            new LexicalElement[] {
                new Token("h"),
                new NonTerminal("A")
        })
        .addRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("L")
        })
        .addRule(
            new NonTerminal("L"),
            new LexicalElement[] {
                new Token("l"),
                new NonTerminal("L")
        })
        .addRule(
            new NonTerminal("L"),
            new LexicalElement[] {
                new Token("o")
        })
        .produceGrammar();
    }

}
