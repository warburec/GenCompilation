package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import lexical_analysis.DelimitedLexicalAnalyser;

public class DelimitedLexicalAnalyserTests {
    
    @Test
    public void whitespaceDelimiterRemoval() {
        String sentence =
        "for (int n : numbers) {\n" +
        "\t//Do stuff\n" +
        "}";
        DelimitedLexicalAnalyser lexAnalyser = new DelimitedLexicalAnalyser();

        String outputString = lexAnalyser.removeDelimiters(sentence);

        String expectedSentence = "for(intn:numbers){//Dostuff}";
        assertEquals(expectedSentence, outputString);
    }

}
