package lexical_analysis;

import java.util.HashMap;
import java.util.Map;

import grammar_objects.*;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    /**
        User-definables/dynamic tokens must have mutually exclusive regex
        This makes the assumption that differentiation of tokens with the same Regex (if present) can be done later (semantic analysis)

        @param whitespaceDelimiters All string to be considered as whitespace (Will not be tokenised)
        @param stronglyReservedWords Words that cannot be part of a user-definable token. Examples: operators, punctuation, etc. (Not including whitespace)
        @param weaklyReservedWords Words that will be tokenised if they do not appear as part of a user-definable token
        @param dynamicTokenRegex A map of classes for dynamic tokens (including identifiers and literals) and the regex that must be met in order to parse them. Note: All potential token sets from the regex must be mutually exclusive
    */
    GeneralLexicalAnalyser(
        String[] whitespaceDelimiters,
        Token[] stronglyReservedWords,
        Token[] weaklyReservedWords,
        Map<Token, String> dynamicTokenRegex
        ) {
        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    GeneralLexicalAnalyser(
        String[] whitespaceDelimiters,
        Token[] stronglyReservedWords,
        Token[] weaklyReservedWords
        ) {
        Map<Token, String> dynamicTokenRegex = new HashMap<>();
        dynamicTokenRegex.put(new Identifier("identifier"), "[a-zA-Z].*");
        dynamicTokenRegex.put(new Literal("string"), "\".*\"");
        dynamicTokenRegex.put(new Literal("integer"), "[0-9]+");
        dynamicTokenRegex.put(new Literal("real"), "[0-9]\\.[0-9]+");
        dynamicTokenRegex.put(new Literal("boolean"), "[true|false]");
        dynamicTokenRegex.put(new Literal("character"), "\'.\'");

        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    private void initialise(String[] whitespaceDelimiters, //Could make this an array of strings if specific strings are considered whitespace/delimiters
        Token[] stronglyReservedWords,
        Token[] weaklyReservedWords,
        Map<Token, String> dynamicTokenRegex) {

        // Initial checking:
        // User-definables/dynamic tokens must have mutually exclusive regex
        validateDynamicTokens();
        //TODO
    }

    private void validateDynamicTokens() {
        //TODO
    }

    @Override
    public Token[] analyse(String sentence) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
}
