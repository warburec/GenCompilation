package grammars.small_grammar;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

/**
 * E->E+B
 * E->E*B
 * E->B
 * B->0
 * B->1
 */
public class SmallGrammar {

    public static Grammar produce() {
        return new GrammarBuilder()
        .setSentinal(new NonTerminal("E"))
        .addRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("+"),
                new NonTerminal("B")
        })
        .addRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("E"),
                new Token("*"),
                new NonTerminal("B")
        })
        .addRule(
            new NonTerminal("E"),
            new LexicalElement[] {
                new NonTerminal("B")
        })
        .addRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("0")
        })
        .addRule(
            new NonTerminal("B"),
            new LexicalElement[] {
                new Token("1")
        })
        .produceGrammar();
    }

}
