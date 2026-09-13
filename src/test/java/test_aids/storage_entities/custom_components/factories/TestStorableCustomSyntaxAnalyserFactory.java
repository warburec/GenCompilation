package test_aids.storage_entities.custom_components.factories;

import component_construction.factories.syntax_analysis.SyntaxAnalyserFactory;
import grammar_objects.GrammarParts;
import syntax_analysis.SyntaxAnalyser;
import test_aids.storage_entities.custom_components.TestStorableCustomSyntaxAnalyser;

public class TestStorableCustomSyntaxAnalyserFactory implements SyntaxAnalyserFactory {

    @Override
    public SyntaxAnalyser produceAnalyser(GrammarParts parts) {
        return new TestStorableCustomSyntaxAnalyser();
    }
    
}
