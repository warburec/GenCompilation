package test_aids.test_grammars.exceptions;

import test_aids.GrammarType;

public class UnsupportedGrammarException extends RuntimeException {

    public UnsupportedGrammarException(GrammarType type) {
        super("The grammar " + type.name() + " is not supported");
    }

}

