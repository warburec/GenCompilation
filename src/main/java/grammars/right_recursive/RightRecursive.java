package grammars.right_recursive;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

public class RightRecursive {
    
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
                new NonTerminal("Property"),
                new NonTerminal("PropertyListTail"),
        })
        
        .addRule(
            new NonTerminal("PropertyListTail"), 
            new LexicalElement[] {
                new NonTerminal("Property"),
                new NonTerminal("PropertyListTail"),
        })
        
        .addRule(
            new NonTerminal("PropertyListTail"), 
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
