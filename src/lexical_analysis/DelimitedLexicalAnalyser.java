package lexical_analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grammar_objects.Token;

public class DelimitedLexicalAnalyser implements LexicalAnalyser {

    private Set<String> delimiters;
    
    /**
     * Default delimiters, whitespace
     */
    public DelimitedLexicalAnalyser() {
        String[] delimiters = new String[] {
            " ", "\t", "\n", "\r"
        };

        setUpDelimiters(delimiters);
    }

    /**
     * Delimiters will be treated like whitespace
     * @param delimiters
     */
    public DelimitedLexicalAnalyser(String[] delimiters) {
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
        String reducedSentence = removeDelimiters(sentence);

        return new NonDelimitedLexicalAnalyser().analyse(reducedSentence);
    }
    
}
