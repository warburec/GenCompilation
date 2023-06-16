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
        dynamicTokenRegex.put("Identifier", new NotEmptyTuple<String, String>("identifier", "[a-z].*"));
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
            new Token("//Do", 2,  6), //No preprocessing
            new Token("stuffs", 2, 10),
            new Token("}", 3, 0)
        };

        assertArrayEquals(expected, actual);
    }

}
