package lexical_analysis;

import grammar_objects.Token;
import helperObjects.*;

public class NonDelimitedLexicalAnalyser  implements LexicalAnalyser {

    //TODO: Tokenise by largest possible matching token
    //Allow RegEx input to apply rules for delimiters etc.
    private NullableTuple<String, String> stringMarkers;
    private Tuple<String, String> integerMarkers;
    private String identifierRegex;

    public NonDelimitedLexicalAnalyser(NullableTuple<String, String> stringMarkers, Tuple<String, String> integerMarkers, String identifierRegex) {
        //TODO

        this.stringMarkers = stringMarkers;
        this.integerMarkers = integerMarkers;
        this.identifierRegex = identifierRegex;
    }

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

}
