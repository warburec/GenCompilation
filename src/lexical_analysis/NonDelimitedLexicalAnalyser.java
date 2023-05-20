package lexical_analysis;

import java.util.*;

import grammar_objects.*;
import helperObjects.*;

//TODO: Create builders for lexical analysers
public class NonDelimitedLexicalAnalyser  implements LexicalAnalyser {

    private NotEmptyTuple<String, String> stringMarkers;
    private NotEmptyTuple<String, String> integerMarkers;
    private NotEmptyTuple<String, String> floatMarkers;
    private NotEmptyTuple<String, String> characterMarkers;
    private String identifierRegex;
    private String[] reservedWords;
    private List<Tuple<Integer, Integer>> identifierMarkers;

    /**
     * 
     * @param stringMarkers Cannot be null
     * @param integerMarkers May be null
     * @param floatMarkers May be null
     * @param characterMarkers Cannot be null, cannot be the same as the string markers
     * @param identifierRegex Will be replaced with "^[a-zA-Z][a-zA-Z0-9]*$" if left as null (alphanumeric, starting with an alphabetic character)
     * @param identifierMarkers Marks the positions of every identifier. If these are not marked identifiers cannot contain any reserved words
     */
    public NonDelimitedLexicalAnalyser(NotEmptyTuple<String, String> stringMarkers,
        NotEmptyTuple<String, String> characterMarkers,
        NotEmptyTuple<String, String> integerMarkers,
        NotEmptyTuple<String, String> floatMarkers,
        String identifierRegex,
        String[] reservedWords,
        List<Tuple<Integer, Integer>> identifierMarkers) {
        
        if(stringMarkers == null) { throw new IllegalArgumentException("String markers must be defined"); }
        this.stringMarkers = stringMarkers;

        if(characterMarkers == null) { throw new IllegalArgumentException("Character markers must be defined"); }
        if(characterMarkers.value1().equals(stringMarkers.value1()) &&
            characterMarkers.value2().equals(stringMarkers.value2())) { 
            throw new IllegalArgumentException("Character markers must be different from string markers"); 
        }
        this.characterMarkers = characterMarkers;

        this.integerMarkers = integerMarkers;
        this.floatMarkers = floatMarkers;

        if(identifierRegex == null) { identifierRegex = "^[a-zA-Z][a-zA-Z0-9]*$"; }
        if(identifierRegex == "") { throw new IllegalArgumentException("Identifier regex must be defined"); }
        this.identifierRegex = identifierRegex;

        this.reservedWords = reservedWords;

        this.identifierMarkers = identifierMarkers;
    }

    @Override
    public Token[] analyse(String sentence) {
        // String a = "identifierName";
        // boolean matches = a.matches(identifierRegex); //For evaluating identifiers

        // Tokenise by largest possible matching token
        // Allow two formats for identifiers: Type identName, or identName {any amount of tokens} literal(with type)
        
        //For each character
            //Look at last characters (max lookback is the length of the largest reserved word)
        

        //Note: Identifiers cannot contain reserved words unless marked
        
        List<Token> tokens = new LinkedList<>();

        Iterator<Character> input = sentence.chars().mapToObj(c -> (char) c).iterator();

        int largestReservedWord = getLargestSize(reservedWords);
        Queue<Character> lookBack = new ArrayDeque<Character>();

        while(getNextChar(input, lookBack, largestReservedWord)) {
            System.out.println(lookBack);
        }

        //Tokenize end of queue


        
        return (Token[])tokens.toArray();
    }

    private int getLargestSize(String[] strings) {
        int largest = 0;

        for (String string : strings) {
            if(string.length() > largest) {
                largest = string.length();
            }
        }

        return largest;
    }

    private boolean getNextChar(Iterator<Character> input, Queue<Character> buffer, int maxBufferLen) {
        try {
            if(buffer.size() == maxBufferLen) {
                buffer.remove();
            }

            buffer.add(input.next());
            return true;
        }
        catch(NoSuchElementException e) {
            return false;
        }
    }
}
