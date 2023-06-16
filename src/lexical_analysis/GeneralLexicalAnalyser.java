package lexical_analysis;

import java.util.*;
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
        generateDynamicRegexPatterns(dynamicTokenRegex);
    }

    private void validateDynamicTokens() {
        // User-definables/dynamic tokens must have mutually exclusive regex
        //TODO
    }

    private void generateDynamicRegexPatterns(Map<String, NotEmptyTuple<String, String>> dynamicTokenRegexStringBased) {
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

        StringHolder holder = new StringHolder();

        int lineNum = 1;
        int columnNum = 0;

        ArrayList<Character> currentCharList = new ArrayList<>();
        String currentTokStr = "";
        for (char c : sentenceChars) {
            currentCharList.add(c);

            columnNum++;

            currentTokStr = getStringRepresentation(currentCharList);

            holder.setString(currentTokStr);
            if(!removeEndingDelimiter(holder)) { continue; }

            String tokWithoutDelim = holder.getString();

            if(!tokWithoutDelim.equals("")) {
                tokenList.addAll(produceTokens(tokWithoutDelim, lineNum, columnNum - currentTokStr.length() + 1)); //+1 for 1-indexed

                if(c == '\n') {
                    lineNum++;
                    columnNum = 0;
                }
            }

            currentCharList.clear();
        }

        if(!currentTokStr.equals("")) {
            tokenList.addAll(produceTokens(currentTokStr, lineNum, columnNum));
        }

        return tokenList.toArray(new Token[tokenList.size()]);
    }

    /**
     * Removes the ending delimiter of a string if it contains one
     * @param string The string to be altered
     * @return Whether a removal occured or not
     */
    private boolean removeEndingDelimiter(StringHolder holder) {
        String string = holder.getString();
        int stringLen = string.length();

        for (String delimiter : whitespaceDelimiters) {
            int delimLength = delimiter.length();

            if(delimLength > stringLen) { continue; }

            String endSubstring = string.substring(stringLen - delimLength, stringLen);

            if(endSubstring.equals(delimiter)) {
                holder.setString(string.substring(0, stringLen - delimLength));
                return true;
            }
        }

        return false;
    }

    private List<Token> produceTokens(String string, int lineNum, int columnNum) {
        List<String> splitByStrongWords = splitAtStronglyReservedWords(string); //TODO: Retain line and column information
        
        List<Token> tokens = new ArrayList<>(4);

        //Match to regex
        for(String part : splitByStrongWords) {
            if(part.equals("")) { continue; }
            tokens.add(produceToken(part, lineNum, columnNum));
        }

        return tokens;
    }

    private Token produceToken(String string, int lineNum, int columnNum) {
        //Check for weakly reserved words
        Token foundWeakReserved = matchWeaklyReservedWord(string, lineNum, columnNum);

        if(foundWeakReserved != null) {return foundWeakReserved; }

        for(Pattern regex : dynamicTokenRegex.keySet()) {
            if(!regex.matcher(string).matches()) { continue; }

            NotEmptyTuple<String, String> details = dynamicTokenRegex.get(regex);

            return DynamicTokenFactory.create(
                details.value1(),
                details.value2(),
                string,
                lineNum,
                columnNum
            );
        }

        throw new RuntimeException("No dynamic token lexemes matched the string \"" + string + "\" at line " + lineNum + " column " + columnNum);
    }

    private Token matchWeaklyReservedWord(String string, int lineNum, int columnNum) {
        for (String weakWord : weaklyReservedWords) {
            if(string.equals(weakWord)) {
                return new Token(string, lineNum, columnNum); //TODO: Consider allowing users to define subtypes of Token to use (factory)
            }
        }

        return null;
    }

    private List<String> splitAtStronglyReservedWords(String string) {
        List<String> substrings = new ArrayList<>(4);
        substrings.add(string);

        List<String> temp = new ArrayList<>(4);

        for (String reservedWord : stronglyReservedWords) {
            temp.clear();

            for (String substring : substrings) {
                for(String part : substring.split(Pattern.quote(reservedWord))) { //TODO: Reserved words could be regex
                    temp.add(part);
                }
            }

            substrings.clear();
            substrings.addAll(temp);
        }

        return substrings;
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
    
    private class StringHolder {
        private String string;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}
