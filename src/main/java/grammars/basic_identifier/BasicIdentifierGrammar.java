package grammars.basic_identifier;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

/**
 * <statement list> := <statement> |
 *                     <statement list> <statement>
 * <statement> := identifier = <element> + <element>;
 * <element> := identifier | number
 */
public class BasicIdentifierGrammar {

    public static Grammar produce() {
        return new GrammarBuilder()
        .setSentinal(new NonTerminal("statement list"))
        .addRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement")
        })
        .addRule(
            new NonTerminal("statement list"),
            new LexicalElement[] {
                new NonTerminal("statement list"),
                new NonTerminal("statement")
        })
        .addRule(
            new NonTerminal("statement"),
            new LexicalElement[] {
                new Identifier("identifier"),
                new Token("="),
                new NonTerminal("element"),
                new Token("+"),
                new NonTerminal("element"),
                new Token(";")
        })
        .addRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Identifier("identifier")
        })
        .addRule(
            new NonTerminal("element"),
            new LexicalElement[] {
                new Literal("number")
        })
        .produceGrammar();
    }

}