package grammars.basic_CLR1;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

public class BasicCLR1Grammar {

    public static Grammar produce() {
        return new GrammarBuilder()
        .setSentinal(new NonTerminal("S"))
        .addRule(
            new NonTerminal("S"), 
            new LexicalElement[] {
                new NonTerminal("X"),
                new NonTerminal("X")
        })
        .addRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("X")
        })
        .addRule(
            new NonTerminal("X"),
            new LexicalElement[] {
                new Token("b")
        })
        .produceGrammar();
    }
}
