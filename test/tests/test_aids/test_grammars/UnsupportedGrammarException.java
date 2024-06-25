package tests.test_aids.test_grammars;

import tests.test_aids.GrammarType;

public class UnsupportedGrammarException extends RuntimeException {

    public UnsupportedGrammarException(GrammarType type) {
        super("The grammar " + type.name() + " is not supported");
    }

}

