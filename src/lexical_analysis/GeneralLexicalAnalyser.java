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
    private Map<Pattern, String> dynamicTokenRegex;

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
     *  @param dynamicTokenRegex The regex objects for dynamic tokens (including identifiers and literals). Note: All potential token sets from the regex must be mutually exclusive
    */
    public GeneralLexicalAnalyser(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex
        ) {
        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    /**
     * Default dynamic tokens:
     * Regex:"[a-zA-Z].*",     Object type:"Identifier", Grammatical name:"identifier" //TODO: Remove object types in JavaDoc
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
        DynamicTokenRegex[] dynamicTokenRegex = {
            new DynamicTokenRegex("[a-zA-Z].*", "identifier"),
            new DynamicTokenRegex("\".*\"", "string"),
            new DynamicTokenRegex("[0-9]+", "integer"),
            new DynamicTokenRegex("[0-9]\\.[0-9]+", "real"),
            new DynamicTokenRegex("[true|false]", "boolean"),
            new DynamicTokenRegex("\'.\'", "character")
        };

        initialise(whitespaceDelimiters, stronglyReservedWords, weaklyReservedWords, dynamicTokenRegex);
    }

    private void initialise(
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex
        ) {
        this.whitespaceDelimiters = whitespaceDelimiters;

        List<String> reservedWords = new ArrayList<>(stronglyReservedWords.length);
        Collections.addAll(reservedWords, stronglyReservedWords);

        this.stronglyReservedWords = reservedWords.toArray(new String[reservedWords.size()]);
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

    private void preprocessDynamicTokenRegex(DynamicTokenRegex[] dynamicTokenRegex) {
        //TODO: User-definables/dynamic tokens must have mutually exclusive regex

        RegexFeatureChecker featureChecker = new RegexFeatureChecker();
        dynamicRegexBookends = new DynamicRegexBookends();

        for (DynamicTokenRegex regex : dynamicTokenRegex) {
            Tuple<String, String> bookends = featureChecker.produceBookends(regex.regex());
            if(bookends == null) { continue; }

            dynamicRegexBookends.addBookends(
                bookends.value1(), 
                bookends.value2(),
                regex.grammaticalName()
            );
        }
    }

    private void generateDynamicRegexPatterns(DynamicTokenRegex[] dynamicTokenRegex) {
        this.dynamicTokenRegex = new HashMap<>();

        for (DynamicTokenRegex regex : dynamicTokenRegex) {
            Pattern regexPattern = Pattern.compile(regex.regex(), Pattern.DOTALL);
            this.dynamicTokenRegex.put(regexPattern, regex.grammaticalName());
        }
    }

    //It may be useful to warn users of dynamic tokens that do not have bookends, thus not allowing them to contain strongly reserved words
    //It may also be useful to allow users to disallow strongly reserved words even if there are specific bookends 
    @Override
    public Token[] analyse(String sentence) {
        char[] sentenceChars = sentence.toCharArray();

        StronglyResRemovalHolder strongRemovalholder = new StronglyResRemovalHolder();
        TokenisationHolder tokenHolder = new TokenisationHolder();

        ArrayList<Character> currentCharList = new ArrayList<>();
        String currentTokStr = "";
        for(int i = 0; i < sentenceChars.length; i++) {
            if(tokenHolder.suppressReservedWords) {
                //Add characters until end regex match
                boolean matchFound = false;
                String firstMatch = "";

                try {
                    String endMatch = "";

                    while(true) {
                        i++;
                        currentCharList.add(sentenceChars[i]);

                        currentTokStr = getStringRepresentation(currentCharList);

                        endMatch = findEndingRegex(tokenHolder.endBookends, currentTokStr);

                        if(matchFound) {
                            if(endMatch == firstMatch) { continue; }
                            
                            i--;
                            currentTokStr = currentTokStr.substring(0, currentTokStr.length() - 1);
                            break;
                        }
                        else {
                            if(endMatch == null) { continue; }

                            matchFound = true;
                            firstMatch = endMatch;
                        }
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    if(matchFound) { break; }
                    
                    throw new LexicalError("Token ended prematurely at line:" + tokenHolder.lineNum + ", column:" + tokenHolder.columnNum + " (end of file)");
                }
                
                tokenHolder.tokenList.add(tokenise(currentTokStr, tokenHolder.startBookend, firstMatch, tokenHolder));
                updateDisplayPosition(tokenHolder, currentTokStr, StringType.Dynamic);

                tokenHolder.suppressReservedWords = false;
                currentCharList.clear();

                continue;
            }

            currentCharList.add(sentenceChars[i]);

            currentTokStr = getStringRepresentation(currentCharList);

            removeStronglyReservedEnding(currentTokStr, strongRemovalholder);

            if(strongRemovalholder.removalType == RemovalType.None) { continue; }
            
            if(!strongRemovalholder.prefix.equals("")) {
                tokeniseSection(strongRemovalholder.prefix, tokenHolder);
            }

            if(tokenHolder.suppressReservedWords) {
                continue;
            }

            if(strongRemovalholder.removalType == RemovalType.StronglyReserved) {
                tokenHolder.tokenList.add(tokeniseStronglyReserved(
                    strongRemovalholder.suffix,
                    tokenHolder
                ));

                updateDisplayPosition(tokenHolder, strongRemovalholder.suffix, StringType.StrongWord);
            }
            else {
                updateDisplayPosition(tokenHolder, strongRemovalholder.suffix, StringType.Delimiter);
            }

            currentCharList.clear();
        }

        if(currentCharList.size() != 0) {
            tokeniseSection(currentTokStr, tokenHolder);
        }

        return tokenHolder.tokenList.toArray(new Token[tokenHolder.tokenList.size()]);
    }

    private void tokeniseSection(String regex, TokenisationHolder tokenHolder) {
        if(matchWeaklyReservedWord(regex, tokenHolder)){
            return;
        }

        while(true) {
            BookendDetails startBookend = findStartingBookend(regex);
            
            //Due to assumption that two non-bookended dynamic tokens cannot be adjacent. If this changes, change this to tokenise without using bookends
            if(startBookend == null) { 
                Token newToken = tokenise(regex, tokenHolder);

                if(newToken == null) {
                    throw new LexicalError("No start bookend found");  //TODO: Make Error more descriptive (follow Google error design structure). "Check all Regex special characters are escaped correctly"
                }

                tokenHolder.tokenList.add(newToken);
                updateDisplayPosition(tokenHolder, regex, StringType.Dynamic);
                
                return;
            }

            if(startBookend.position != BookendPosition.Start) {
                tokenHolder.tokenList.add(tokeniseWithoutBookends(regex.substring(0, startBookend.startIndex)));
                regex = regex.substring(startBookend.startIndex);
            }

            String afterStart = regex.substring(startBookend.length);
            BookendDetails endBookend = findMatchingEndBookend(startBookend.regex, afterStart);

            if(endBookend == null) {
                Token token = tokenise(regex, tokenHolder);

                if(token != null) {
                    tokenHolder.tokenList.add(token);
                    updateDisplayPosition(tokenHolder, regex, StringType.Dynamic);
                    return;
                }

                //Not closed bookends
                tokenHolder.suppressReservedWords = true;
                tokenHolder.startBookend = startBookend.regex;
                tokenHolder.endBookends = dynamicRegexBookends.getEndRegex(startBookend.regex);
                return;
            }
            
            if(endBookend.position == BookendPosition.End) {
                tokenHolder.tokenList.add(tokenise(
                    regex.substring(0, startBookend.length + endBookend.startIndex + endBookend.length), 
                    startBookend.regex, 
                    endBookend.regex,
                    tokenHolder
                ));

                return;
            }
            
            //Keep trying end bookend until failure or positioned at end of regex
            String endRegex = endBookend.regex;
            String remainingString = afterStart.substring(endBookend.startIndex); 
            int index = endBookend.startIndex + 1;
            boolean edgeFound = false;
            String[] parts;

            while(index < afterStart.length() - 1) {
                remainingString = afterStart.substring(index);

                parts = java.util.regex.Pattern.compile(endRegex, Pattern.DOTALL).split(remainingString, 2);

                if(!parts[0].equals("")) {
                    edgeFound = true;
                    break;
                }

                index++;
            }
            
            if(!edgeFound) {
                //Tokenise all as one token
                tokenHolder.tokenList.add(tokenise(
                    regex, 
                    startBookend.regex, 
                    endBookend.regex,
                    tokenHolder
                ));

                updateDisplayPosition(tokenHolder, regex, StringType.Dynamic);
                return;
            }
            
            index--;

            int endOfStartIndex = startBookend.startIndex + startBookend.length;

            BookendDetails finalMatch = findMatchingEndBookend(
                startBookend.regex,
                regex.substring(endOfStartIndex + index)
            ); //TODO: Alter to use the same endBookend, not just any potential one , only useful for length

            String stringToTokenise = regex.substring(0, endOfStartIndex + index + finalMatch.length);

            tokenHolder.tokenList.add(tokenise(
                stringToTokenise, 
                startBookend.regex, 
                endBookend.regex,
                tokenHolder
            ));

            updateDisplayPosition(tokenHolder, stringToTokenise, StringType.Dynamic);

            regex = regex.substring(endOfStartIndex + index + finalMatch.length);
        }
    }

    private BookendDetails findStartingBookend(String string) {
        boolean matchFound = false;
        String startRegexMatched = "";
        int indexOfStart = -1;
        int lengthOfStart = -1;

        for(String start : dynamicRegexBookends.startToEnd.keySet()) {
            String[] splitString = string.split("(?<=" + start + ")" + "|(?=" + start + ")", 2); //TODO: Use splitAround

            if(splitString.length == 1) { continue; }

            String part1 = splitString[0];
            String part2 = splitString[1];

            if(part1.length() >= indexOfStart && matchFound) { continue; }

            matchFound = true;

            if(part1.matches(start)) {
                indexOfStart = 0;
                lengthOfStart = part1.length();
            }
            else {
                indexOfStart = part1.length();
                lengthOfStart = part2.length();
            }

            startRegexMatched = start;
        }

        if(!matchFound) {
            return null;
        }

        BookendPosition startPosition = BookendPosition.Middle;
        if(indexOfStart == 0) {
            startPosition = BookendPosition.Start;
        }
        else if(indexOfStart + lengthOfStart == string.length()) {
            startPosition = BookendPosition.End;
        }

        return new BookendDetails(startRegexMatched, indexOfStart, lengthOfStart, startPosition);
    }

    // private String[] splitAround(String string, String regex) {
    //     return string.split("(?<=" + regex + ")" + "|(?=" + regex + ")", 2);
    // }

    private BookendDetails findMatchingEndBookend(String startBookendRegex, String string) {
        Set<String> potentialEnds = dynamicRegexBookends.getEndRegex(startBookendRegex);

        boolean matchFound = false;
        int shortestLength = -1;
        String endingRegexMatched = "";
        int indexOfEnding = -1;
        int lengthOfEnding = -1;

        for(String ending : potentialEnds) {
            String[] splitString = string.split(ending +"|(?<=" + ending + ")", 2); //TODO: Use splitAround

            if(splitString.length == 1) { continue; }

            matchFound = true;

            String beforeEnding = splitString[0];
            String endingMatch = splitString[1];

            int fullLength = beforeEnding.length() + endingMatch.length();

            if(fullLength < shortestLength || shortestLength == -1) {
                shortestLength = fullLength;
                indexOfEnding = beforeEnding.length();
                lengthOfEnding = string.length() - beforeEnding.length() - endingMatch.length();
                endingRegexMatched = ending;
            }
        }

        if(!matchFound) {
            return null;
        }

        BookendPosition endPosition = BookendPosition.Middle;
        if(indexOfEnding + lengthOfEnding == string.length()) {
            endPosition = BookendPosition.End;
        }

        return new BookendDetails(endingRegexMatched, indexOfEnding, lengthOfEnding, endPosition);
    }

    private String findEndingRegex(Set<String> endBookends, String string) {
        for(String ending : endBookends) {
            if(dynamicRegexBookends.endRegexPattern(ending)
                .matcher(string).matches()
            ) {
                return ending;
            }
        }

        return null;
    }

    private Token tokeniseStronglyReserved(String word, TokenisationHolder tokenHolder) {
        return new Token(word, tokenHolder.lineNum, tokenHolder.columnNum);
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

    private Token tokenise(String string, String startBookend, String endBookend, TokenisationHolder tokenHolder) {
        String grammaticalName = dynamicRegexBookends.getTokenDetails(startBookend, endBookend);

        //Don't forget reserved words
        return new DynamicToken(
            grammaticalName,
            string,
            tokenHolder.lineNum,
            tokenHolder.columnNum
        );
    }

    private Token tokenise(String string, TokenisationHolder tokenHolder) {
        for(Pattern regex : dynamicTokenRegex.keySet()) {
            if(regex.matcher(string).matches()) {
                String grammaticalName = dynamicTokenRegex.get(regex);

                return new DynamicToken(
                    grammaticalName,
                    string,
                    tokenHolder.lineNum,
                    tokenHolder.columnNum
                );
            }
        }

        return null;
    }

    private Token tokeniseWithoutBookends(String substring) {
        throw new RuntimeException("Not implemented");
    }


    private boolean matchWeaklyReservedWord(String string, TokenisationHolder tokenHolder) {
        for (String weakWord : weaklyReservedWords) {
            if(string.equals(weakWord)) {
                tokenHolder.tokenList.add(new Token(string, tokenHolder.lineNum, tokenHolder.columnNum)); //TODO: Consider allowing users to define subtypes of Token to use (factory)

                updateDisplayPosition(tokenHolder, weakWord, StringType.WeakWord);
                return true;
            }
        }

        return false;
    }

    private void updateDisplayPosition(TokenisationHolder tokenHolder, String analysedWord, StringType wordType) {
        List<Integer> newlines = getNewlinePositions(analysedWord, wordType);
        tokenHolder.lineNum += newlines.size();

        if(newlines.size() != 0) {
            tokenHolder.columnNum = analysedWord.length();
            tokenHolder.columnNum -= newlines.get(newlines.size() - 1);
        }
        else {
            tokenHolder.columnNum += analysedWord.length();
        }
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

        //Note: These mark the current position of analysis, not the position for tokens (these will be offset backwards from this position)
        //These are also 1-indexed
        public int lineNum = 1;
        public int columnNum = 1;
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
        private Map<NotEmptyTuple<String,String>, String> bookendsToDetails;
        private Map<String, Pattern> regexForEndingMatches;

        public DynamicRegexBookends() {
            startToEnd = new HashMap<>();
            endToStart = new HashMap<>();
            bookendsToDetails = new HashMap<>();
            regexForEndingMatches = new HashMap<>();
        }

        public void addBookends(String start, String end, String grammaticalName) {
            NotEmptyTuple<String,String> bookendTuple = new NotEmptyTuple<String,String>(start, end);
            String existingDetails = bookendsToDetails.get(bookendTuple);

            if(existingDetails != null) {
                throw new BookendConflict(existingDetails, grammaticalName, bookendTuple);
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

            regexForEndingMatches.put(end, Pattern.compile(".*" + end + "\\Z", Pattern.DOTALL));

            bookendsToDetails.put(bookendTuple, grammaticalName);
        }
        
        public Set<String> getEndRegex(String startBookend) {
            return startToEnd.get(startBookend);
        }

        // public Set<String> getStartRegex(String endBookend) {
        //     return endToStart.get(endBookend);
        // }

        public String getTokenDetails(String startBookend, String endBookend) {
            return bookendsToDetails.get(new NotEmptyTuple<String,String>(startBookend, endBookend));
        }

        public Pattern endRegexPattern(String regex) {
            return regexForEndingMatches.get(regex);
        }
    }

    public class BookendConflict extends LexicalError {
        public BookendConflict(String name1, String name2, Tuple<String, String> bookends) {
            super("The tokens \"" + name1 + "\" and \"" + name2 + "\" have the same bookends <\"" + bookends.value1() + "\", \"" + bookends.value2() + "\">");
        }
    }
}
