package tests.test_aids;

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
