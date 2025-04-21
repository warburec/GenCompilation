package helper_objects;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import component_construction.builders.grammar_objects.GrammarBuilder;
import grammar_objects.*;

public class GrammarTests {
    
    @Test
    public void equality() {
        Grammar expectedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .produceGrammar();

        Grammar producedGrammar = new GrammarBuilder()
        .setSentinal(new NonTerminal("A"))
        .addRule(new ProductionRule(
            new NonTerminal("A"), 
            new LexicalElement[] {
                new Token("b")
            }
        ))
        .produceGrammar();


        assertEquals(expectedGrammar, producedGrammar);
    }
}
