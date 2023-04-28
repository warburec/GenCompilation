package tests.test_aids;

import tests.test_aids.grammar_generators.IntegerCompGrammar;

public class GrammarGenerator {
    
    public static GrammarParts generateParts(Grammar grammar) {
        switch(grammar) {
            case IntegerComputation:
                return new IntegerCompGrammar().getParts();
            default:
                throw new UnknownGrammarException();
        }
    }

    public enum Grammar {
        IntegerComputation
    }
}
