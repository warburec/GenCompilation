package test_aids.storage_entities.custom_components;

import grammar_objects.Token;

/**
 * Identical class to {@code TestCustomLexicalAnalyser} but does not implement {@code LexicalAnalyser}
 */
public class TestCustomFalseLexicalAnalyser {

    public Token[] analyse(String sentence) {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }

}
