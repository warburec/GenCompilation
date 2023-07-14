package lexical_analysis;

import java.util.*;
import java.util.regex.Pattern;

import grammar_objects.*;
import helperObjects.NotEmptyTuple;
import helperObjects.RegexFeatureChecker;
import helperObjects.Tuple;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    private String[] whitespaceDelimiters;
    private String[] stronglyReservedWords;
    private String[] weaklyReservedWords;
    private Map<Pattern, NotEmptyTuple<String, String>> dynamicTokenRegex;

    private Map<String, List<Integer>> whitespaceNewlinePositions;
    private Map<String, List<Integer>> stronglyReservedWordNewlinePositions;
    private Map<String, List<Integer>> weaklyReservedWordNewlinePositions;

    private Map<String, String> dynamicRegexBookends;

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
     *  @param dynamicTokenRegex The regex for dynamic tokens (including identifiers and literals) along with a tuple of the class name and grammatical name of each token. Note: All potential token sets from the regex must be mutually exclusive
    */
    public GeneralLexicalAnalyser(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        Map<String, NotEmptyTuple<String, String>> dynamicTokenRegex //TODO: Try passing in classes directly or use enum
        ) {
        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    /**
     * Default dynamic tokens:
     * Regex:"[a-zA-Z].*",     Object type:"Identifier", Grammatical name:"identifier"
     * Regex:"\".*\"",         Object type:"Literal",    Grammatical name:"string"
     * Regex:"[0-9]+",         Object type:"Literal",    Grammatical name:"integer"
     * Regex:"[0-9]\\.[0-9]+", Object type:"Literal",    Grammatical name:"real"
     * Regex:"[true|false]",   Object type:"Literal",    Grammatical name:"boolean"
     * Regex:"\'.\'",          Object type:"Literal",    Grammatical name:"character"
     * @param whitespaceDelimiters
     * @param stronglyReservedWords
     * @param weaklyReservedWords
     */
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

        preprocessDynamicTokenRegex(dynamicTokenRegex);

        cacheNewlinePositions(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords);
        generateDynamicRegexPatterns(dynamicTokenRegex);
    }

    private void cacheNewlinePositions(
        String[] whitespaceDelimiters, 
        String[] stronglyReservedWords, 
        String[] weaklyReservedWords
        ) {
        whitespaceNewlinePositions = new HashMap<>();
        for(String delim : whitespaceDelimiters) {
            whitespaceNewlinePositions.put(delim, getNewlinePositions(delim));
        }

        stronglyReservedWordNewlinePositions = new HashMap<>();
        for(String strongWord : stronglyReservedWords) {
            stronglyReservedWordNewlinePositions.put(strongWord, getNewlinePositions(strongWord));
        }

        weaklyReservedWordNewlinePositions = new HashMap<>();
        for(String weakWord : weaklyReservedWords) {
            weaklyReservedWordNewlinePositions.put(weakWord, getNewlinePositions(weakWord));
        }
    }

    private void preprocessDynamicTokenRegex(Map<String, NotEmptyTuple<String, String>> dynamicTokenRegex) {
        //TODO: User-definables/dynamic tokens must have mutually exclusive regex

        RegexFeatureChecker featureChecker = new RegexFeatureChecker();
        dynamicRegexBookends = new HashMap<>();

        for (String regex : dynamicTokenRegex.keySet()) {
            Tuple<String, String> bookends = featureChecker.produceBookends(regex);
            if(bookends == null) { continue; }

            if(dynamicRegexBookends.containsKey(bookends.value1())) { throw new MutualInclusionException();}

            dynamicRegexBookends.put(bookends.value1(), bookends.value2());
        }
    }

    private void generateDynamicRegexPatterns(Map<String, NotEmptyTuple<String, String>> dynamicTokenRegexStringBased) {
        dynamicTokenRegex = new HashMap<>();

        for (String regexString : dynamicTokenRegexStringBased.keySet()) {
            Pattern regexPattern = Pattern.compile(regexString, Pattern.DOTALL);
            dynamicTokenRegex.put(regexPattern, dynamicTokenRegexStringBased.get(regexString));
        }
    }

    //TODO: Suppress skipping characters when whithin token bookends, e.g. '"' and '"' for strings
    //It may be useful to warn users of dynamic tokens that do not have bookends, thus not allowing them to contain strongly reserved words
    //It may also be useful to allow users to disallow strongly reserved words even if there are specific bookends 
    @Override
    public Token[] analyse(String sentence) {
        List<Token> tokenList = new LinkedList<>();
        char[] sentenceChars = sentence.toCharArray();

        StronglyResRemovalHolder holder = new StronglyResRemovalHolder();

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

            //TODO: Check startcurrentTokStr for bookend instances

            if(!holder.prefix.equals("")) {
                tokenList.add(produceToken(holder.prefix, lineNum, columnNum + 1 - currentTokStr.length())); //+1 for 1-indexing

                Tuple<Integer, Integer> endPos = getEndingPosition(holder.prefix, StringType.Dynamic, lineNum, columnNum - holder.suffix.length());
                lineNum = endPos.value1();
                columnNum = endPos.value2() + holder.suffix.length();
            }

            StringType suffixType = StringType.Delimiter;
            if(holder.removalType == RemovalType.StronglyReserved) {
                tokenList.add(tokeniseStronglyReserved(holder.suffix, lineNum, columnNum  + 1 - holder.suffix.length())); //+1 for 1-indexing
                suffixType = StringType.StrongWord;
            }

            if(!holder.suffix.equals("")) {
                Tuple<Integer, Integer> endPos = getEndingPosition(holder.suffix, suffixType, lineNum, columnNum);
                lineNum = endPos.value1();
                columnNum = endPos.value2();
            }

            currentCharList.clear();
        }

        if(!currentCharList.isEmpty()) {
            currentTokStr = getStringRepresentation(currentCharList);

            removeStronglyReservedEnding(currentTokStr, holder);

            if(!holder.prefix.equals("")) {
                tokenList.add(produceToken(holder.prefix, lineNum, columnNum + 1 - currentTokStr.length())); //+1 for 1-indexing

                Tuple<Integer, Integer> endPos = getEndingPosition(holder.prefix, StringType.Dynamic, lineNum, columnNum - holder.suffix.length());
                lineNum = endPos.value1();
                columnNum = endPos.value2() + holder.suffix.length();
            }

            if(holder.removalType == RemovalType.StronglyReserved) {
                tokenList.add(tokeniseStronglyReserved(holder.suffix, lineNum, columnNum + 1 - holder.suffix.length())); //+1 for 1-indexing
            }
        }

        return tokenList.toArray(new Token[tokenList.size()]);
    }

    private String matchStartingBookend(String currentTokStr) {
        for (String startBookend : dynamicRegexBookends.keySet()) {
            if(currentTokStr.matches(startBookend)) { return startBookend; }
        }

        return null;
    }

    private boolean matchesEndBookend(String endString, String startBookend) {
        return endString.matches(dynamicRegexBookends.get(startBookend));
    }

    /**
     * Gets the ending position of the analyser if the position was displayed on-screen
     * @param string The string to be analysed
     * @param lineNum The initial line number of analysis
     * @param columnNum The initial column number of analysis
     * @return The final perceived position of the analyser in the tuple {line number, column number}
     */
    private Tuple<Integer, Integer> getEndingPosition(String string, StringType stringType, int lineNum, int columnNum) {
        List<Integer> newlinePositions = getNewlinePositions(string, stringType);

        if(newlinePositions.size() > 0) {
            lineNum += newlinePositions.size();
            
            int lastPos = newlinePositions.get(newlinePositions.size() - 1);
            columnNum = string.length() - lastPos - 1; 
        }

        return new NotEmptyTuple<Integer, Integer>(lineNum, columnNum);
    }

    private Token tokeniseStronglyReserved(String word, int lineNum, int columnNum) {
        return new Token(word, lineNum, columnNum);
    }

    /**
     * Removes the ending delimiter of a string if it contains one, placing the result in the given holder object
     * @param string The string to be altered
     * @param holder A holder object to hold the string split at reserved words and the type of removal that occurred
     */
    private void removeStronglyReservedEnding(String string, StronglyResRemovalHolder holder) {
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

    private List<Integer> getNewlinePositions(String string, StringType stringType) {
        switch(stringType) {
            case Dynamic:
                return getNewlinePositions(string);
            case Delimiter:
                return whitespaceNewlinePositions.get(string);
            case StrongWord:
                return stronglyReservedWordNewlinePositions.get(string);
            case WeakWord:
                return weaklyReservedWordNewlinePositions.get(string);
            default:
                throw new RuntimeException("Unsupported string type");
        }
    }

    private List<Integer> getNewlinePositions(String string) {
        List<Integer> newlinePositions = new ArrayList<>(string.length());

        int currentIndex = string.indexOf("\n");
        while (currentIndex >= 0) {
            newlinePositions.add(currentIndex);
            currentIndex = string.indexOf("\n", currentIndex + 1);
        }

        return newlinePositions;
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
    
    private class StronglyResRemovalHolder {
        public String prefix;
        public String suffix;
        public RemovalType removalType;
    }

    private enum RemovalType {
        None,
        Delimiter,
        StronglyReserved
    }

    private enum StringType {
        Delimiter,
        StrongWord,
        WeakWord,
        Dynamic
    }

    public class MutualInclusionException extends RuntimeException {
        public MutualInclusionException() {
            super("Not all dynamic token lexemes are mutually exclusive");
            //TODO: Highlight rules causing conflict
        }
    }
}
