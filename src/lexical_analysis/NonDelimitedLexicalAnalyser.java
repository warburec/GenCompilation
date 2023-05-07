package lexical_analysis;

import grammar_objects.Token;
import helperObjects.*;

public class NonDelimitedLexicalAnalyser  implements LexicalAnalyser {

    private NotEmptyTuple<String, String> stringMarkers;
    private NotEmptyTuple<String, String> integerMarkers;
    private NotEmptyTuple<String, String> floatMarkers;
    private NotEmptyTuple<String, String> characterMarkers;
    private String identifierRegex;
    private String[] reserverdWords;

    /**
     * 
     * @param stringMarkers Cannot be null
     * @param integerMarkers May be null
     * @param floatMarkers May be null
     * @param characterMarkers Cannot be null, cannot be the same as the string markers
     * @param identifierRegex Will be replaced with "^[a-zA-Z][a-zA-Z0-9]*$" if left as null (alphanumeric, starting with an alphabetic character)
     */
    public NonDelimitedLexicalAnalyser(NotEmptyTuple<String, String> stringMarkers,
        NotEmptyTuple<String, String> characterMarkers,
        NotEmptyTuple<String, String> integerMarkers,
        NotEmptyTuple<String, String> floatMarkers, 
        String identifierRegex,
        String[] reserverdWords) {
        
        if(stringMarkers == null) { throw new IllegalArgumentException("String markers must be defined"); }
        this.stringMarkers = stringMarkers;

        if(characterMarkers == null) { throw new IllegalArgumentException("Character markers must be defined"); }
        if(characterMarkers.value1().equals(stringMarkers.value1()) &&
            characterMarkers.value2().equals(stringMarkers.value2())) { 
            throw new IllegalArgumentException("Character markers must be different from string markers"); 
        }
        this.characterMarkers = characterMarkers;

        this.integerMarkers = integerMarkers;
        this.floatMarkers = floatMarkers;

        if(identifierRegex == null) { identifierRegex = "^[a-zA-Z][a-zA-Z0-9]*$"; }
        if(identifierRegex == "") { throw new IllegalArgumentException("Identifier regex must be defined"); }
        this.identifierRegex = identifierRegex;

        this.reserverdWords = reserverdWords;
    }

    @Override
    public Token[] analyse(String sentence) {
        // String a = "identifierName";
        // boolean matches = a.matches(identifierRegex); //For evaluating identifiers

        // Tokenise by largest possible matching token
        // Allow two formats for identifiers, Type identName, or identName {any amount of tokens} literal(with type)
    }

}
