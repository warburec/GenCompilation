package lexical_analysis;

import java.util.*;

import grammar_objects.Token;
import helperObjects.*;

public class DelimitedLexicalAnalyser implements LexicalAnalyser {

    private Set<String> delimiters;
    
    private NotEmptyTuple<String, String> stringMarkers;
    private NotEmptyTuple<String, String> characterMarkers;
    private NotEmptyTuple<String, String> integerMarkers;
    private NotEmptyTuple<String, String> floatMarkers;
    private String identifierRegex;
    private String[] reservedWords;
    private List<Tuple<Integer, Integer>> identifierMarkers;
    
    /**
     * Default delimiters, whitespace
     */
    public DelimitedLexicalAnalyser(
        NotEmptyTuple<String, String> stringMarkers,
        NotEmptyTuple<String, String> characterMarkers,
        NotEmptyTuple<String, String> integerMarkers,
        NotEmptyTuple<String, String> floatMarkers,
        String identifierRegex,
        String[] reservedWords) {
        String[] delimiters = new String[] {
            " ", "\t", "\n", "\r"
        };

        setUpInnerAnalyserVariables(stringMarkers, characterMarkers, integerMarkers, floatMarkers, identifierRegex, reservedWords, identifierMarkers);
        setUpDelimiters(delimiters);
    }

    /**
     * Delimiters will be treated like whitespace
     * @param delimiters
     */
    public DelimitedLexicalAnalyser(
        String[] delimiters,
        NotEmptyTuple<String, String> stringMarkers,
        NotEmptyTuple<String, String> characterMarkers,
        NotEmptyTuple<String, String> integerMarkers,
        NotEmptyTuple<String, String> floatMarkers,
        String identifierRegex,
        String[] reservedWords) {
        setUpInnerAnalyserVariables(stringMarkers, characterMarkers, integerMarkers, floatMarkers, identifierRegex, reservedWords, identifierMarkers);
        setUpDelimiters(delimiters);
    }

    private void setUpDelimiters(String[] delimiters) {
        this.delimiters = new HashSet<>();

        for (String delimiter : delimiters) {
            if(delimiter == "" | delimiter == null) {
                throw new IllegalArgumentException("Delimiters cannot be null or an empty string");
            }

            this.delimiters.add(delimiter);
        }
    }

    private void setUpInnerAnalyserVariables(
        NotEmptyTuple<String, String> stringMarkers,
        NotEmptyTuple<String, String> characterMarkers,
        NotEmptyTuple<String, String> integerMarkers,
        NotEmptyTuple<String, String> floatMarkers,
        String identifierRegex,
        String[] reservedWords,
        List<Tuple<Integer, Integer>> identifierMarkers) {
        this.stringMarkers = stringMarkers;
        this.characterMarkers = characterMarkers;
        this.integerMarkers = integerMarkers;
        this.floatMarkers = floatMarkers;
        this.identifierRegex = identifierRegex;
        this.reservedWords = reservedWords;
        this.identifierMarkers = identifierMarkers;
    }

    public String removeDelimiters(String sentence) {
        List<int[]> deletionRanges = new ArrayList<>(sentence.length() / 2);

        for (int i = 0; i < sentence.length(); i++) {
            for (String delimiter : delimiters) { //TODO:Handle delimiters contained within other delimiters
                if(i < delimiter.length()) { continue; }

                int start = i - delimiter.length();
                int end = i;
                if(!sentence.substring(start, end).equals(delimiter)) { continue; }

                if(deletionRanges.size() != 0) {
                    int[] lastRange = deletionRanges.get(deletionRanges.size() - 1);

                    if(lastRange[1] >= start) {
                        lastRange[1] = end;
                        continue;
                    }
                }

                deletionRanges.add(new int[] {start, end});
            }
        }

        StringBuilder stringBuilder = new StringBuilder(sentence);
        for (int i = deletionRanges.size() - 1; i >= 0; i--) {
            int[] range = deletionRanges.get(i);
            stringBuilder.delete(range[0], range[1]);
        }

        return stringBuilder.toString();
    }
    
    @Override
    public Token[] analyse(String sentence) {
        //TODO: mark identifier positions
        String reducedSentence = removeDelimiters(sentence);

        return new NonDelimitedLexicalAnalyser(
            stringMarkers, 
            characterMarkers, 
            integerMarkers, 
            floatMarkers, 
            reducedSentence, 
            reservedWords, 
            identifierMarkers)
        .analyse(reducedSentence);
    }
    
}
