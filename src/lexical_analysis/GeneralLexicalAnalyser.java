package lexical_analysis;

import java.util.*;
import java.util.regex.Pattern;

import grammar_objects.*;
import helperObjects.*;

public class GeneralLexicalAnalyser implements LexicalAnalyser {

    /*
     * Notes:
     *  It may be possible to make exceptions such that tokenising a strongly reserved word can be paused until it is definitely not a dynaic token bookend
     *      [This may remove the need for strongly and weakly reserved words
     *      Allowing analysis of sCode{//Code}e \n e //Text tagged with "e" (Like html tags)
     */

    private String[] whitespaceDelimiters;
    private String[] stronglyReservedWords;
    private String[] weaklyReservedWords;
    private Map<Pattern, NotEmptyTuple<String, String>> dynamicTokenRegex;

    private Map<String, List<Integer>> whitespaceNewlinePositions;
    private Map<String, List<Integer>> stronglyReservedWordNewlinePositions;
    private Map<String, List<Integer>> weaklyReservedWordNewlinePositions;

    private DynamicRegexBookends dynamicRegexBookends;

    /**
     *  User-definables/dynamic tokens must have mutually exclusive regex
     *  This makes the assumption that differentiation of tokens with the same Regex (if present) can be done later (semantic analysis)
     *  The empty string between two delimiters is not considered as a token
     * 
     *  Line numbers will be made according to "\n" instances. Note: These do not have to be considered as whitespace in the grammar
     * 
     *  @param whitespaceDelimiters All string to be considered as whitespace (Will not be tokenised)
     *  @param stronglyReservedWords Words that cannot be part of any user-definable token bookends (start and end). Examples: operators, punctuation, etc. (Not including whitespace)
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
        dynamicRegexBookends = new DynamicRegexBookends();

        for (String regex : dynamicTokenRegex.keySet()) {
            Tuple<String, String> bookends = featureChecker.produceBookends(regex);
            if(bookends == null) { continue; }

            Tuple<String, String> tokenDetails = dynamicTokenRegex.get(regex);
            dynamicRegexBookends.addBookends(
                bookends.value1(), 
                bookends.value2(),
                tokenDetails.value1(),
                tokenDetails.value2()
            );
        }
    }

    private void generateDynamicRegexPatterns(Map<String, NotEmptyTuple<String, String>> dynamicTokenRegexStringBased) {
        dynamicTokenRegex = new HashMap<>();

        for (String regexString : dynamicTokenRegexStringBased.keySet()) {
            Pattern regexPattern = Pattern.compile(regexString, Pattern.DOTALL);
            dynamicTokenRegex.put(regexPattern, dynamicTokenRegexStringBased.get(regexString));
        }
    }

    //It may be useful to warn users of dynamic tokens that do not have bookends, thus not allowing them to contain strongly reserved words
    //It may also be useful to allow users to disallow strongly reserved words even if there are specific bookends 
    @Override
    public Token[] analyse(String sentence) {
        char[] sentenceChars = sentence.toCharArray();

        StronglyResRemovalHolder strongRemovalholder = new StronglyResRemovalHolder();
        TokenisationHolder tokenHolder = new TokenisationHolder();

        //Note: These mark the current position of analysis, not the position for tokens (these will be offset backwards from this position)
        //These are also 1-indexed
        int lineNum = 1;
        int columnNum = 0;

        ArrayList<Character> currentCharList = new ArrayList<>();
        String currentTokStr = "";
        for(int i = 0; i < sentenceChars.length; i++) {
            currentCharList.add(sentenceChars[i]);
            
            columnNum++;

            currentTokStr = getStringRepresentation(currentCharList);

            if(tokenHolder.suppressReservedWords) {
                //Add characters until end regex match
                //TODO: Handle end of file
                String endMatch = null;
                while(endMatch == null) {
                    i++;
                    currentCharList.add(sentenceChars[i]);
            
                    columnNum++;

                    currentTokStr = getStringRepresentation(currentCharList);

                    endMatch = findEndingRegex(tokenHolder.endBookends, currentTokStr);
                }

                tokenHolder.tokenList.add(tokenise(currentTokStr, tokenHolder.startBookend, endMatch, lineNum, columnNum));

                tokenHolder.suppressReservedWords = false;
            }
            else {
                removeStronglyReservedEnding(currentTokStr, strongRemovalholder);
            }

            if(strongRemovalholder.removalType == RemovalType.None) { continue; }

            if(strongRemovalholder.prefix.equals("")) {
                tokenHolder.tokenList.add(tokeniseStronglyReserved(
                    strongRemovalholder.suffix,
                    lineNum,
                    columnNum + 1 - strongRemovalholder.suffix.length() //+1 for 1-indexing
                ));
                continue;
            }
            
            tokeniseSection(strongRemovalholder.prefix, lineNum, columnNum, tokenHolder);

            if(!tokenHolder.suppressReservedWords) {
                tokenHolder.tokenList.add(tokeniseStronglyReserved(
                    strongRemovalholder.suffix,
                    lineNum,
                    columnNum + 1 - strongRemovalholder.suffix.length() //+1 for 1-indexing
                ));
            }
        }

        return tokenHolder.tokenList.toArray(new Token[tokenHolder.tokenList.size()]);
    }

    private void tokeniseSection(String regex, int endinglineNum, int endingColumnNum, TokenisationHolder holder) {
        while(true) {
            BookendDetails startBookend = findStartingBookend(regex);

            //TODO: Handle weakly reserved words
            
            //Due to assumption that two non-bookended dynamic tokens cannot be adjacent. If this changes, change this to tokenise without using bookends
            if(startBookend == null) { throw new LexicalError("No start bookend found"); }

            if(startBookend.position != BookendPosition.Start) {
                holder.tokenList.add(tokeniseWithoutBookends(regex.substring(0, startBookend.startIndex)));
                regex = regex.substring(startBookend.startIndex);
            }

            String afterStart = regex.substring(startBookend.length);
            BookendDetails endBookend = findMatchingEndBookend(startBookend.regex, afterStart);

            if(endBookend == null) {
                //Not closed bookends
                holder.suppressReservedWords = true;
                holder.startBookend = startBookend.regex;
                holder.endBookends = dynamicRegexBookends.getEndRegex(startBookend.regex);
                return;
            }
            else {
                holder.tokenList.add(tokenise(
                    regex.substring(0, endBookend.startIndex + endBookend.length), 
                    startBookend.regex, 
                    endBookend.regex,
                    endinglineNum,
                    endingColumnNum
                ));

                regex = regex.substring(endBookend.startIndex + endBookend.length);

                if(regex == "") { return; }
            }
        }
    }

    private BookendDetails findStartingBookend(String string) {
        return null;
    }

    private BookendDetails findMatchingEndBookend(String startBookendRegex, String string) {
        Set<String> potentialEnds = dynamicRegexBookends.getEndRegex(startBookendRegex);

        int shortestLength = -1;
        String fullStringMatch = "";
        String endingRegexMatched = "";
        int indexOfEnding = -1;
        int lengthOfEnding = -1;

        for(String ending : potentialEnds) {
            String[] splitString = string.split(ending +"|(?<=" + ending + ")", 2);
            String beforeEnding = splitString[0];
            String endingMatch = splitString[1];

            int fullLength = beforeEnding.length() + endingMatch.length();

            if(fullLength < shortestLength || shortestLength == -1) {
                shortestLength = fullLength;
                fullStringMatch = beforeEnding + endingMatch;
                indexOfEnding = beforeEnding.length();
                lengthOfEnding = endingMatch.length();
                endingRegexMatched = ending;
            }
        }

        if(fullStringMatch.equals(string)) {
            return null;
        }

        BookendPosition endPosition = BookendPosition.Middle;
        if(indexOfEnding + lengthOfEnding == string.length()) {
            endPosition = BookendPosition.End;
        }

        return new BookendDetails(endingRegexMatched, indexOfEnding, lengthOfEnding, endPosition);
    }

    private String findEndingRegex(Set<String> endBookends, String string) {
        for (String ending : endBookends) {
            if(string.matches(".*" + ending + "\\Z")) {
                return ending;
            }
        }

        return null;
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


    private Token tokenise(String string, String startBookend, String endBookend, int lineNum, int columnNum) {
        //Get type and grammar name of token
        Tuple <String, String> tokenDetails = dynamicRegexBookends.getTokenDetails(startBookend, endBookend);

        //Don't forget reserved words
        return DynamicTokenFactory.create(
            tokenDetails.value1(),
            tokenDetails.value2(),
            string,
            lineNum,
            columnNum
        );
    }

    private Token tokeniseWithoutBookends(String substring) {
        return null;
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
    
    private class TokenisationHolder {
        public List<Token> tokenList = new LinkedList<>();
        public boolean suppressReservedWords = false;
        public String startBookend;
        public Set<String> endBookends = new HashSet<>();
    }

    private class StronglyResRemovalHolder {
        public String prefix;
        public String suffix;
        public RemovalType removalType;
    }

    private class BookendDetails {
        public BookendPosition position;
        public String regex;
        public int length;
        public int startIndex;

        public BookendDetails(String regex, int startIndex, int length, BookendPosition position) {
            this.position = position;
            this.regex = regex;
            this.length = length;
            this.startIndex = startIndex;
        }
    }

    private enum BookendPosition {
        Start,
        Middle,
        End
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

    private class DynamicRegexBookends {
        private Map<String, Set<String>> startToEnd;
        private Map<String, Set<String>> endToStart;
        private Map<NotEmptyTuple<String,String>, NotEmptyTuple<String,String>> bookendsToDetails; //Bookends -> <Type, GrammaticalName>

        public DynamicRegexBookends() {
            startToEnd = new HashMap<>();
            endToStart = new HashMap<>();
            bookendsToDetails = new HashMap<>();
        }

        public void addBookends(String start, String end, String tokenType, String grammaticalName) {
            NotEmptyTuple<String,String> bookendTuple = new NotEmptyTuple<String,String>(start, end);
            NotEmptyTuple<String,String> existingDetails = bookendsToDetails.get(bookendTuple);

            if(existingDetails != null) {
                throw new BookendConflict(existingDetails.value2(), grammaticalName, bookendTuple);
            }

            Set<String> startSet = startToEnd.get(start);
            Set<String> endSet = endToStart.get(end);

            if(startSet == null) {
                startToEnd.put(start, new HashSet<>());
                startToEnd.get(start).add(end);
            }
            else {
                startSet.add(end);
            }

            if(endSet == null) {
                endToStart.put(end, new HashSet<>());
                endToStart.get(end).add(start);
            }
            else {
                endSet.add(end);
            }

            bookendsToDetails.put(bookendTuple, new NotEmptyTuple<String,String>(tokenType, grammaticalName));
        }
        
        public Set<String> getEndRegex(String startBookend) {
            return startToEnd.get(startBookend);
        }

        public Set<String> getStartRegex(String endBookend) {
            return endToStart.get(endBookend);
        }

        public Tuple<String,String> getTokenDetails(String startBookend, String endBookend) {
            return bookendsToDetails.get(new NotEmptyTuple<String,String>(startBookend, endBookend));
        }
    }

    public class BookendConflict extends LexicalError {
        public BookendConflict(String name1, String name2, Tuple<String, String> bookends) {
            super("The tokens \"" + name1 + "\" and \"" + name2 + "\" have the same bookends <\"" + bookends.value1() + "\", \"" + bookends.value2() + "\">");
        }
    }
}
