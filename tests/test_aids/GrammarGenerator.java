package tests.test_aids;

import grammar_objects.GrammarParts;

public class GrammarGenerator {
    
    public static GrammarParts generateParts(Grammar grammar) {
        switch(grammar) {
            // case IntegerComputation:
            //     return new IntegerCompGrammar().getParts();
            default:
                throw new UnknownGrammarException();
        }
    }

    public enum Grammar {
        IntegerComputation
    }
}
