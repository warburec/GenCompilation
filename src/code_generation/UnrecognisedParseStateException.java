package code_generation;

import syntax_analysis.parsing.ParseState;

public class UnrecognisedParseStateException extends RuntimeException {
    public UnrecognisedParseStateException(ParseState parseState) {
        super("A parse state was found with the unrecognised type " + parseState.getClass() + "\n" +
            parseState.toString());
    }
}
