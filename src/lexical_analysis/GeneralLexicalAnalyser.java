package lexical_analysis;

import java.util.*;
import java.util.regex.Pattern;

import grammar_objects.*;
import helperObjects.NotEmptyTuple;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    private String[] whitespaceDelimiters; //TODO: Cache newline positions
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
            Pattern regexPattern = Pattern.compile(regexString, Pattern.DOTALL);
            dynamicTokenRegex.put(regexPattern, dynamicTokenRegexStringBased.get(regexString));
        }
    }

    @Override
    public Token[] analyse(String sentence) {
        List<Token> tokenList = new LinkedList<>();
        char[] sentenceChars = sentence.toCharArray();

        StrongResRemovalHolder holder = new StrongResRemovalHolder();

        //Note: These mark the current position of analysis, not the position for tokens (these will be offset backwards from this position)
        //These are also 1-indexed
        int lineNum = 1;
        int columnNum = 0;

        ArrayList<Character> currentCharList = new ArrayList<>();
        String currentTokStr = "";
        for (char c : sentenceChars) {
            currentCharList.add(c);
            
            columnNum++;

            currentTokStr = getStringRepresentation(currentCharList);

            removeStronglyReservedEnding(currentTokStr, holder);

            if(holder.removalType == RemovalType.None) { continue; }

            if(!holder.prefix.equals("")) {
                tokenList.add(produceToken(holder.prefix, lineNum, columnNum  + 1 - currentTokStr.length())); //+1 for 1-indexing

                int newlinePos = getNewlinePosition(holder.prefix); //TODO: Fix the assumption that prefix/suffix strings will only contain one newline
                if(newlinePos != -1) {
                    lineNum++;
                    columnNum = holder.prefix.length() - newlinePos;
                }
            }

            if(holder.removalType == RemovalType.StronglyReserved) {
                tokenList.add(tokeniseStronglyReserved(holder.suffix, lineNum, columnNum  + 1 - holder.suffix.length())); //+1 for 1-indexing
            }

            if(!holder.suffix.equals("")) {
                int newlinePos = getNewlinePosition(holder.suffix);
                if(newlinePos != -1) {
                    lineNum++;
                    columnNum = holder.suffix.length() - newlinePos - 1;
                }
            }

            currentCharList.clear();
        }

        if(!currentCharList.isEmpty()) {
            currentTokStr = getStringRepresentation(currentCharList);

            removeStronglyReservedEnding(currentTokStr, holder);

            if(!holder.prefix.equals("")) {
                tokenList.add(produceToken(holder.prefix, lineNum, columnNum + 1 - currentTokStr.length())); //+1 for 1-indexing

                int newlinePos = getNewlinePosition(holder.prefix);
                if(newlinePos != -1) {
                    lineNum++;
                    columnNum = holder.prefix.length() - newlinePos;
                }
            }

            if(holder.removalType == RemovalType.StronglyReserved) {
                tokenList.add(tokeniseStronglyReserved(holder.suffix, lineNum, columnNum + 1 - holder.suffix.length())); //+1 for 1-indexing
            }
        }

        return tokenList.toArray(new Token[tokenList.size()]);
    }

    private Token tokeniseStronglyReserved(String word, int lineNum, int columnNum) {
        return new Token(word, lineNum, columnNum);
    }

    /**
     * Removes the ending delimiter of a string if it contains one, placing the result in the given holder object
     * @param string The string to be altered
     * @param holder A holder object to hold the string split at reserved words and the type of removal that occurred
     */
    private void removeStronglyReservedEnding(String string, StrongResRemovalHolder holder) {
        int stringLen = string.length();

        for (String delimiter : whitespaceDelimiters) {
            int delimLength = delimiter.length();

            if(delimLength > stringLen) { continue; }

            String endSubstring = string.substring(stringLen - delimLength, stringLen);

            if(endSubstring.equals(delimiter)) {
                String startSubstring = string.substring(0, stringLen - delimLength);

                holder.prefix = startSubstring;
                holder.suffix = delimiter;

                holder.removalType = RemovalType.Delimiter;

                return;
            }
        }

        for (String strongWord : stronglyReservedWords) {
            int wordLength = strongWord.length();

            if(wordLength > stringLen) { continue; }

            String endSubstring = string.substring(stringLen - wordLength, stringLen);

            if(endSubstring.equals(strongWord)) {
                String startSubstring = string.substring(0, stringLen - wordLength);

                holder.prefix = startSubstring;
                holder.suffix = endSubstring;

                holder.removalType = RemovalType.StronglyReserved;

                return;
            }
        }

        holder.prefix = string;
        holder.removalType = RemovalType.None;
    }

    private int getNewlinePosition(String string) {
        return string.indexOf('\n');
    }

    private Token produceToken(String string, int lineNum, int columnNum) {
        Token foundWeakReserved = matchWeaklyReservedWord(string, lineNum, columnNum);

        if(foundWeakReserved != null) { return foundWeakReserved; }

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

    private String getStringRepresentation(ArrayList<Character> list)
    {    
        StringBuilder builder = new StringBuilder(list.size());

        for(Character c : list) {
            builder.append(c);
        }

        return builder.toString();
    }
    
    private class StrongResRemovalHolder {
        public String prefix;
        public String suffix;
        public RemovalType removalType;
    }

    private enum RemovalType {
        None,
        Delimiter,
        StronglyReserved
    }
}
