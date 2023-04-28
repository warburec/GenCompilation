package tests.test_aids.grammar_generators;

public class UnsupportedSentenceException extends RuntimeException {

    public UnsupportedSentenceException(String objectToBeAccessed, String givenSentence) {
        super("There is/are no supported " + objectToBeAccessed + " for the sentence \"" + givenSentence + "\"");
    }

}
