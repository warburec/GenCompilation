package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

import grammar_objects.*;
import helperObjects.NotEmptyTuple;
import lexical_analysis.*;


public class GeneralLexicalAnalyserTests {

    @Test
    public void whitespaceDelimiterRemoval() {
        String sentence =
        "for (int n : numbers) {\n" +
        "    //Do stuff\n" +
        "}";

        String[] delims = new String[] {
            " ",
            "\t",
            "\r\n",
            "\n"
        };

        String[] stronglyReserved = new String[] {
            "(",
            ")",
            ":",
            "{",
            "}"
        };

        String[] weaklyReserved = new String[] {
            "for"
        };

        Map<String, NotEmptyTuple<String, String>> dynamicTokenRegex = new HashMap<String, NotEmptyTuple<String, String>>();
        dynamicTokenRegex.put("[^\"0-9].*", new NotEmptyTuple<String, String>("Identifier", "identifier")); //TODO: Reformat to Regex:<Class, grammarName> 
        dynamicTokenRegex.put("\".*\"", new NotEmptyTuple<String, String>("Literal", "string"));
        dynamicTokenRegex.put("[0-9]+[\\.[0.9]+]?", new NotEmptyTuple<String, String>("Literal", "number"));
        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new Token("for", 1, 1),
            new Token("(", 1, 5),
            new Identifier("identifier", "int", 1, 6),
            new Identifier("identifier", "n", 1, 10),
            new Token(":", 1, 12),
            new Identifier("identifier", "numbers", 1, 14),
            new Token(")", 1, 21),
            new Token("{", 1, 23),
            new Identifier("identifier", "//Do", 2, 5), //No preprocessing
            new Identifier("identifier", "stuffs", 2, 10), // ^
            new Token("}", 3, 1)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

}
