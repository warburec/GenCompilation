package test_aids.storage_entities.custom_components;

import grammar_objects.Token;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

public class TestCustomSyntaxAnalyser implements SyntaxAnalyser {

    @Override
    public ParseState analyse(Token[] inputTokens) throws ParseFailedException {
        throw new UnsupportedOperationException("Unimplemented method 'analyse'");
    }
    
}
