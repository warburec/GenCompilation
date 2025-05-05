package component_construction.factories.grammar;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import component_construction.builders.grammar_objects.GrammarBuilder;
import component_construction.factories.grammar.BNFConvertor.InvalidEscapeCharacterException;
import grammar_objects.*;

public class BNFConvertorTests {

    @Test
    public void singleRule() {
        String bnf  = """
            A -> b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void multipleRules() {
        String bnf  = """
            A -> b
            B -> c
            C -> d
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("B"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("C"), 
            new LexicalElement[] {
                new Token("d")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void multipleTokens() {
        String bnf  = """
            A -> a b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("a"),
                new Token("b")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void nonTerminalInRule() {
        String bnf  = """
            A -> N b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new NonTerminal("N"),
                new Token("b")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void taggedTokens() {
        String bnf  = """
            A -> t:b t:B
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b"),
                new Token("B")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void taggedNonTerminals() {
        String bnf  = """
            n:a -> n:b n:B
            n:b -> c
            B -> d
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("a"))
        .addRule(new ProductionRule(
            new NonTerminal("a"), 
            new LexicalElement[] {
                new NonTerminal("b"),
                new NonTerminal("B")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("b"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("B"), 
            new LexicalElement[] {
                new Token("d")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void tagAsTokenPrefix() {
        String bnf  = """
            A -> t:t:b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("t:b")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void tagAsNonTerminalPrefix() {
        String bnf  = """
            n:A -> n:n:B
            n:n:B -> c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new NonTerminal("n:B"),
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("n:B"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedSpaceTokens() {
        String bnf  = """
            A -> \\  b\\  t:\\ \\\s
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token(" "),
                new Token("b "),
                new Token("  ")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedSpaceNonTerminal() {
        String bnf  = """
            n:\\ A -> B\\ \\  n:C\\ C
            B\\ \\  -> a
            n:C\\ C -> b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal(" A"))
        .addRule(new ProductionRule(
            new NonTerminal(" A"), 
            new LexicalElement[] {
                new NonTerminal("B  "),
                new NonTerminal("C C")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("B  "), 
            new LexicalElement[] {
                new Token("a")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("C C"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedNewlineTokens() {
        String bnf  = """
            A -> \\\n b\\\n t:\\\n\\\n
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("\n"),
                new Token("b\n"),
                new Token("\n\n")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedNewlineNonTerminal() {
        String bnf  = """
            n:\\\nA -> B\\\n\\\n n:C\\\nC
            B\\\n\\\n -> a
            n:C\\\nC -> b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("\nA"))
        .addRule(new ProductionRule(
            new NonTerminal("\nA"), 
            new LexicalElement[] {
                new NonTerminal("B\n\n"),
                new NonTerminal("C\nC")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("B\n\n"), 
            new LexicalElement[] {
                new Token("a")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("C\nC"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .produceGrammar();
        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void arrowInTokens() {
        String bnf  = """
            A -> -> t:a-> \\ ->\\\s
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("->"),
                new Token("a->"),
                new Token(" -> ")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void arrowInNonTerminals() {
        String bnf  = """
            n:->A -> N-> n:N-> b
            n:N-> -> c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("->A"))
        .addRule(new ProductionRule(
            new NonTerminal("->A"), 
            new LexicalElement[] {
                new NonTerminal("N->"),
                new NonTerminal("N->"),
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("N->"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void emptyTokenUsage() {
        String bnf  = """
            A -> B C \\e b t:
            B -> t:
            C -> \\e
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new NonTerminal("B"),
                new NonTerminal("C"),
                new EmptyToken(),
                new Token("b"),
                new EmptyToken()
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("B"), 
            new LexicalElement[] {
                new EmptyToken()
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("C"), 
            new LexicalElement[] {
                new EmptyToken()
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void escapedSpaceBeforeArrow() {
        String bnf  = """
            A\\  -> b
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A "))
        .addRule(new ProductionRule(
            new NonTerminal("A "), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalOr() {
        String bnf  = """
            A -> b | c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalEscapedOr() {
        String bnf  = """
            A -> b \\| c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b"),
                new Token("|"),
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }


    @Test
    public void validEscapes() {
        String bnf  = """
            A -> \\ \\\n \\\t \\e \\|
            """;
        
        assertDoesNotThrow(() -> new BNFConvertor(bnf));
    }
    
    @Test
    public void invalidEscape() {
        String bnf  = """
            A -> \\b
            """;
        
        assertThrows(InvalidEscapeCharacterException.class, () -> new BNFConvertor(bnf));
    }

    @Test
    public void singleNonTerminalAlternativesWithoutOr() {
        String bnf  = """
            A -> b
            A -> c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalAlternativesMultiLine() {
        String bnf  = """
            A -> b
            | c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalAlternativesMultiLineVariableSpacing() {
        String bnf  = """
            A -> b
             | c
                | d
            \t| e
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("d")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("e")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalAlternativesEmptyLine() {
        String bnf  = """
            A -> b
            |
            | c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new EmptyToken()
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }

    @Test
    public void singleNonTerminalEmptyInlineOr() {
        String bnf  = """
            A -> b | | c
            """;
        
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new EmptyToken()
            }
        ))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("c")
            }
        ))
        .produceGrammar();

        
        Grammar producedGrammar = new BNFConvertor(bnf).produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }
}