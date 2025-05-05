package grammars.basic_SLR1;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

/**
 * S –> AA    
 * A –> aA
 * A –> b
 */
public class BasicSLR1Grammar {

    public static Grammar produce() {
        return new GrammarBuilder()
        .setSentinal(new NonTerminal("S"))
        .addRule(
            new NonTerminal("S"),
            new LexicalElement[] {
                new NonTerminal("A"),
                new NonTerminal("A")
            }
        )
        .addRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("a"),
                new NonTerminal("A")
            }
        )
        .addRule(
            new NonTerminal("A"),
            new LexicalElement[] {
                new Token("b")
            }
        )
        .produceGrammar();
    }

}
