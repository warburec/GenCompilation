package lexical_analysis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import grammar_objects.*;
import helperObjects.NotEmptyTuple;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    private String[] whitespaceDelimiters;
    private String[] stronglyReservedWords;
    private String[] weaklyReservedWords;
    private Map<Pattern, NotEmptyTuple<String, String>> dynamicTokenRegex;

    /**
     *  User-definables/dynamic tokens must have mutually exclusive regex
     *  This makes the assumption that differentiation of tokens with the same Regex (if present) can be done later (semantic analysis)
     *  The empty string between two delimiters is not considered as a token
     * 
     *  Line numbers will be made according to "\n" instances. Note: These do not have to be considered as whitespace in the grammar
     * 
     *  @param whitespaceDelimiters All string to be considered as whitespace (Will not be tokenised)
     *  @param stronglyReservedWords Words that cannot be part of a user-definable token. Examples: operators, punctuation, etc. (Not including whitespace)
     *  @param weaklyReservedWords Words that will be tokenised if they do not appear as part of a user-definable token
     *  @param dynamicTokenRegex A map of classes for dynamic tokens (including identifiers and literals) and the regex that must be met in order to parse them. Note: All potential token sets from the regex must be mutually exclusive
    */
    public GeneralLexicalAnalyser(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        Map<String, NotEmptyTuple<String, String>> dynamicTokenRegex //TODO: Try passing in classes directly
        ) {
        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    public GeneralLexicalAnalyser(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords
        ) {
        Map<String, NotEmptyTuple<String, String>> dynamicTokenRegex = new HashMap<>();
        dynamicTokenRegex.put("[a-zA-Z].*", new NotEmptyTuple<String, String>("Identifier", "identifier"));
        dynamicTokenRegex.put("\".*\"", new NotEmptyTuple<String, String>("Literal", "string"));
        dynamicTokenRegex.put("[0-9]+", new NotEmptyTuple<String, String>("Literal", "integer"));
        dynamicTokenRegex.put("[0-9]\\.[0-9]+", new NotEmptyTuple<String, String>("Literal", "real"));
        dynamicTokenRegex.put("[true|false]", new NotEmptyTuple<String, String>("Literal", "boolean"));
        dynamicTokenRegex.put("\'.\'", new NotEmptyTuple<String, String>("Literal", "character"));

        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    private void initialise(
        String[] whitespaceDelimiters, //Could make this an array of strings if specific strings are considered whitespace/delimiters
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        Map<String, NotEmptyTuple<String, String>> dynamicTokenRegex
        ) {
        this.whitespaceDelimiters = whitespaceDelimiters;
        this.stronglyReservedWords = stronglyReservedWords;
        this.weaklyReservedWords = weaklyReservedWords;

        validateDynamicTokens();
        generateRegexMatchers(dynamicTokenRegex);
    }

    private void validateDynamicTokens() {
        // User-definables/dynamic tokens must have mutually exclusive regex
        //TODO
    }

    private void generateRegexMatchers(Map<String, NotEmptyTuple<String, String>> dynamicTokenRegexStringBased) {
        dynamicTokenRegex = new HashMap<>();

        for (String regexString : dynamicTokenRegexStringBased.keySet()) {
            Pattern regexPattern = Pattern.compile(regexString);
            dynamicTokenRegex.put(regexPattern, dynamicTokenRegexStringBased.get(regexString));
        }
    }

    @Override
    public Token[] analyse(String sentence) {
        List<Token> tokenList = new LinkedList<>();
        char[] sentenceChars = sentence.toCharArray();

        int lineNum = 1;
        int columnNum = 0;

        ArrayList<Character> currentCharList = new ArrayList<>();
        String currentTokStr = "";
        for (char c : sentenceChars) {
            currentCharList.add(c);

            columnNum++;

            currentTokStr = getStringRepresentation(currentCharList);

            if(!removeEndingDelimiter(currentTokStr)) { continue; }

            if(currentTokStr.equals("")) { 
                continue; 
            }
            else {
                tokenList.add(produceToken(currentTokStr, lineNum, columnNum));

                if(c == 'n') {
                    lineNum++;
                    columnNum = 0;
                }
            }
        }

        if(!currentTokStr.equals("")) {
            tokenList.add(produceToken(currentTokStr, lineNum, columnNum));
        }

        return (Token[])tokenList.toArray();
    }

    /**
     * Removes the ending delimiter of a string if it contains one
     * @param string The string to be altered
     * @return Whether a removal occured or not
     */
    private boolean removeEndingDelimiter(String string) {
        int stringLen = string.length();

        for (String delimiter : whitespaceDelimiters) {
            int delimLength = delimiter.length();

            if(delimLength > stringLen) { continue; }

            String endSubstring = string.substring(stringLen - delimLength, stringLen);

            if(endSubstring.equals(delimiter)) {
                string = string.substring(0, stringLen - delimLength);
                return true;
            }
        }

        return false;
    }

    private Token produceToken(String string, int lineNum, int columnNum) {
        String strongWordFound = containsStronglyReservedWord(string);
        if(strongWordFound != null) {
            throw new RuntimeException("Dynamic token found containing the strongly reserved word \"" + strongWordFound + "\"");
        }
        
        //Match to regex
        for(Pattern regex : dynamicTokenRegex.keySet()) {
            if(regex.matcher(string).matches()) {
                NotEmptyTuple<String, String> details = dynamicTokenRegex.get(regex);

                return DynamicTokenFactory.create(
                    details.value1(),
                    details.value2(),
                    string,
                    lineNum,
                    columnNum
                );
            }
        }

        return null;
    }

    private String containsStronglyReservedWord(String string) {
        for (String reservedWord : stronglyReservedWords) {
            if(string.contains(reservedWord)) { return reservedWord; }
        }

        return null;
    }

    /* https://stackoverflow.com/questions/6324826/converting-arraylist-of-characters-to-a-string */
    private String getStringRepresentation(ArrayList<Character> list)
    {    
        StringBuilder builder = new StringBuilder(list.size());
        for(Character ch: list)
        {
            builder.append(ch);
        }
        return builder.toString();
    }
    
}
