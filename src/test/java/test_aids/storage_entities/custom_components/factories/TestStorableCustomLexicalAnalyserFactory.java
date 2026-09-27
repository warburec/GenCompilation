package test_aids.storage_entities.custom_components.factories;

import component_construction.factories.lexical_analysis.LexicalAnalyserFactory;
import lexical_analysis.DynamicTokenRegex;
import lexical_analysis.LexicalAnalyser;
import test_aids.storage_entities.custom_components.TestStorableCustomLexicalAnalyser;

public class TestStorableCustomLexicalAnalyserFactory implements LexicalAnalyserFactory {

    @Override
    public LexicalAnalyser produceAnalyser(
        String[] whitespaceDelimiters, 
        String[] stronglyReservedWords,
        String[] weaklyReservedWords, 
        DynamicTokenRegex[] dynamicTokenRegex
    ) {
        return new TestStorableCustomLexicalAnalyser();
    }
    
}
