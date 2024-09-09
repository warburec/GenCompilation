import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import grammar_objects.*;
import helperObjects.NullInputException;
import lexical_analysis.*;


public class GeneralLexicalAnalyserTests {

    @Test
    public void nullDelims() {
        String[] delims = null;

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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };


        assertThrows(NullInputException.class, () -> new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        ));
    }

    @Test
    public void nullStronglyReserved() {
        String[] delims = new String[] {
            " ",
            "\t",
            "\r\n",
            "\n"
        };

        String[] stronglyReserved = null;

        String[] weaklyReserved = new String[] {
            "for"
        };

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };


        assertThrows(NullInputException.class, () -> new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        ));
    }

    @Test
    public void nullWeaklyReserved() {
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

        String[] weaklyReserved = null;

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };


        assertThrows(NullInputException.class, () -> new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        ));
    }

    @Test
    public void nullDynamics() {
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

        DynamicTokenRegex[] dynamicTokenRegex = null;


        assertThrows(NullInputException.class, () -> new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        ));
    }

    @Test
    public void nullSentence() {
        String sentence = null;

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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        assertThrows(NullInputException.class, () -> lexAnalyser.analyse(sentence));
    }
    
    @Test
    public void normalUseTest() {
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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

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
            new DynamicToken("identifier", "int", 1, 6),
            new DynamicToken("identifier", "n", 1, 10),
            new Token(":", 1, 12),
            new DynamicToken("identifier", "numbers", 1, 14),
            new Token(")", 1, 21),
            new Token("{", 1, 23),
            new DynamicToken("identifier", "//Do", 2, 5), //No preprocessing
            new DynamicToken("identifier", "stuffs", 2, 10), // ^
            new Token("}", 3, 1)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void emptyTest() {
        String sentence = "";

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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void whitespaceOnly() {
        String sentence =
        "   \n" +
        "\t\t ";

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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void strongWordsOnly() {
        String sentence =
        "()((}}:::{:()}";

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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new Token("(", 1, 1),
            new Token(")", 1, 2),
            new Token("(", 1, 3),
            new Token("(", 1, 4),
            new Token("}", 1, 5),
            new Token("}", 1, 6),
            new Token(":", 1, 7),
            new Token(":", 1, 8),
            new Token(":", 1, 9),
            new Token("{", 1, 10),
            new Token(":", 1, 11),
            new Token("(", 1, 12),
            new Token(")", 1, 13),
            new Token("}", 1, 14),
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }
    
    @Test
    public void multiCharacterStrongWords() {
        String sentence =
        "for (int n : numbers)-{\n" +
        "    //Do stuff\n" +
        "}";

        String[] delims = new String[] {
            " ",
            "\t",
            "\r\n",
            "\n"
        };

        String[] stronglyReserved = new String[] {
            "(int",
            ")-{",
            ":",
            "{",
            "}"
        };

        String[] weaklyReserved = new String[] {
            "for"
        };

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new Token("for", 1, 1),
            new Token("(int", 1, 5),
            new DynamicToken("identifier", "n", 1, 10),
            new Token(":", 1, 12),
            new DynamicToken("identifier", "numbers", 1, 14),
            new Token(")-{", 1, 21),
            new DynamicToken("identifier", "//Do", 2, 5), //No preprocessing
            new DynamicToken("identifier", "stuffs", 2, 10), // ^
            new Token("}", 3, 1)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void weakWordInDynamicToken() {
        String sentence =
        "for (int n : numbers) {\n" +
        "    //Dofor stuff\n" +
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

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

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
            new DynamicToken("identifier", "int", 1, 6),
            new DynamicToken("identifier", "n", 1, 10),
            new Token(":", 1, 12),
            new DynamicToken("identifier", "numbers", 1, 14),
            new Token(")", 1, 21),
            new Token("{", 1, 23),
            new DynamicToken("identifier", "//Dofor", 2, 5), //No preprocessing
            new DynamicToken("identifier", "stuffs", 2, 13), // ^
            new Token("}", 3, 1)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void newlineWithinTokens() {
        String sentence =
        "a\n" +
        "wwb\n" +
        "c";

        String[] delims = new String[] {
            "w"
        };

        String[] stronglyReserved = new String[] {
        };

        String[] weaklyReserved = new String[] {
        };

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex(".+", "identifier")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("identifier", "a\n", 1, 1),
            new DynamicToken("identifier", "b\nc", 2, 3)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void multipleNewlineWithinTokens() {
        String sentence =
        "a\n" +
        "\n" +
        "wwb\n" +
        "\n" +
        "d\n" +
        "c";

        String[] delims = new String[] {
            "w"
        };

        String[] stronglyReserved = new String[] {
        };

        String[] weaklyReserved = new String[] {
        };

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex(".+", "identifier")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("identifier", "a\n\n", 1, 1),
            new DynamicToken("identifier", "b\n\nd\nc", 3, 3)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void standardStringMatches() {
        String sentence =
        "\"ab cde\" \n" +
        "\"bcd\n" +
        "dcb cdb\" + zzz";

        String[] delims = new String[] {
            " ", "\n"
        };

        String[] stronglyReserved = new String[] {
            "+"
        };

        String[] weaklyReserved = new String[] {
        };

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[^\"].*[^\"]", "ident")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("string", "\"ab cde\"", 1, 1),
            new DynamicToken("string", "\"bcd\ndcb cdb\"", 2, 1),
            new Token("+", 3, 9),
            new DynamicToken("ident", "zzz", 3, 11)
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void contiguousDynamics() {
        String sentence =
        "\"a\"{b}{cd}\"e\"";

        String[] delims = new String[] {
        };

        String[] stronglyReserved = new String[] {
        };

        String[] weaklyReserved = new String[] {
        };

        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("\\{.*\\}", "single set")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("string", "\"a\"", 1, 1),
            new DynamicToken("single set", "{b}", 1, 4),
            new DynamicToken("single set", "{cd}", 1, 7),
            new DynamicToken("string", "\"e\"", 1, 11),
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    public void dynamicsFromStrongWords() {
        String sentence =
        "forwhile = 0;\n";

        String[] delims = new String[] {
            " ",
            "\n"
        };

        String[] stronglyReserved = new String[] {
            "=",
            ";"
        };

        String[] weaklyReserved = new String[] {
            "for",
            "while"
        };

        DynamicTokenRegex[] dynamicTokenRegex = {        
            new DynamicTokenRegex("[^\"0-9].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+[\\.[0.9]+]?", "number")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );
        

        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("identifier", "forwhile", 1, 1),
            new Token("=", 1, 10),
            new DynamicToken("number", "0", 1, 12),
            new Token(";", 1, 13),
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    //TODO: Make tests for: same start bookend but different ends, and different start bookends but same ends
    //^^^^ Add assertions for lexical errors and bookend errors.

    @Test
    @Disabled("Failing - disabled for now")
    public void sameStartingBookend() {
        String sentence = "0000 0aaaa 0000a";

        String[] delims = new String[] {
            " ",
            "\n"
        };

        String[] stronglyReserved = new String[] {
            "=",
            ";"
        };

        String[] weaklyReserved = new String[] {
            "for",
            "while"
        };

        DynamicTokenRegex[] dynamicTokenRegex = {        
            new DynamicTokenRegex("[0-9]+", "number"),
            new DynamicTokenRegex("[0-9]+[a-zA-Z]+", "numStr")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );


        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("number", "0000", 1, 1),
            new DynamicToken("numStr", "0aaaa", 1, 6),
            new DynamicToken("numStr", "0000a", 1, 12),
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }

    @Test
    @Disabled("Failing - disabled for now")
    public void sameEndingBookend() {
        String sentence = "0000 aaaa0 a0000";
        
        String[] delims = new String[] {
            " ",
            "\n"
        };

        String[] stronglyReserved = new String[] {
            "=",
            ";"
        };

        String[] weaklyReserved = new String[] {
            "for",
            "while"
        };

        DynamicTokenRegex[] dynamicTokenRegex = {        
            new DynamicTokenRegex("[0-9]+", "number"),
            new DynamicTokenRegex("[a-zA-Z]+[0-9]+", "strNum")
        };

        LexicalAnalyser lexAnalyser = new GeneralLexicalAnalyser(
            delims,
            stronglyReserved,
            weaklyReserved,
            dynamicTokenRegex
        );


        Token[] actual = lexAnalyser.analyse(sentence);


        Token[] expected = new Token[] {
            new DynamicToken("number", "0000", 1, 1),
            new DynamicToken("strNum", "aaaa0", 1, 6),
            new DynamicToken("strNum", "a0000", 1, 12),
        };

        assertArrayEquals(expected, actual);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].getLineNumber(), actual[i].getLineNumber());
            assertEquals(expected[i].getColumnNumber(), actual[i].getColumnNumber());
        }
    }
}
