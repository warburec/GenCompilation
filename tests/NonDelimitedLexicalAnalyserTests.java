package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import grammar_objects.Identifier;
import grammar_objects.Token;
import helperObjects.NotEmptyTuple;
import lexical_analysis.NonDelimitedLexicalAnalyser;

public class NonDelimitedLexicalAnalyserTests {
    
    @Test
    public void whitespaceDelimiterRemoval() {
        String sentence =
        "for (int n : numbers) {\n" +
        "    //Do stuff\n" +
        "}";
        NotEmptyTuple<String, String> stringMarkers = new NotEmptyTuple<String, String>("\"", "\"");
        NotEmptyTuple<String, String> characterMarkers = new NotEmptyTuple<String, String>("\'", "\'");
        NonDelimitedLexicalAnalyser lexAnalyser = new NonDelimitedLexicalAnalyser(
            stringMarkers,
            characterMarkers,
            null,
            null,
            null,
            null,
            null);
        
        Token[] actual = lexAnalyser.analyse(sentence);

        Token[] expected = new Token[] {
            new Token("for", 1, 0),
            new Token("(", 1, 4),
            new Token("int", 1, 5),
            new Identifier("identifier", "n"),
            new Token(":", 1, 11),
            new Identifier("identifier", "numbers"),
            new Token(")", 1, 21),
            new Token("{", 1, 23),
            //Comment not Tokenised
            new Token("}", 3, 0)
        };

        assertArrayEquals(expected, actual);
    }

}
