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
        dynamicTokenRegex.put("[^\"[0-9]].*", new NotEmptyTuple<String, String>("Identifier", "identifier")); //TODO: Reformat to Regex:<Class, grammarName> 
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
            new Token("for", 1, 0),
            new Token("(", 1, 4),
            new Identifier("identifier", "int"),
            new Identifier("identifier", "n"),
            new Token(":", 1, 11),
            new Identifier("identifier", "numbers"),
            new Token(")", 1, 21),
            new Token("{", 1, 23),
            new Identifier("identifier", "//Do"), //No preprocessing
            new Identifier("identifier", "stuffs"), // ^
            new Token("}", 3, 0)
        };

        assertArrayEquals(expected, actual);
    }

}
