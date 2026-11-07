package grammars.left_recursive;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

public class LeftRecursive {

    public static Grammar produce() {
        return new GrammarBuilder()
        .addRule(
            new NonTerminal("SectionBlock"), 
            new LexicalElement[] {
                new Token("SECTION"),
                new Identifier("Identifier"),
                new Token("{"),
                new NonTerminal("PropertyList"),
                new Token("}")
        })
        
        .addRule(
            new NonTerminal("PropertyList"), 
            new LexicalElement[] {
                new NonTerminal("PropertyListHead"),
                new NonTerminal("Property")
        })
        
        .addRule(
            new NonTerminal("PropertyListHead"), 
            new LexicalElement[] {
                new NonTerminal("PropertyListHead"),
                new NonTerminal("Property")
        })
        
        .addRule(
            new NonTerminal("PropertyListHead"), 
            new LexicalElement[] {
                new EmptyToken()
        })

        .addRule(
            new NonTerminal("Property"), 
            new LexicalElement[] {
                new Identifier("Identifier"),
                new Token("="),
                new Literal("Literal"),
                new Token(";")
        })

        .produceGrammar();
    }

}
