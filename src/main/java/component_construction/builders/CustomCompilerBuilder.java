package component_construction.builders;

import component_construction.custom_components.*;

public class CustomCompilerBuilder extends CompilerBuilderTemplate<CustomCompilerBuilder, CustomCompiler> {

    @Override
    protected CustomCompilerBuilder getThis() {
        return this;
    }

    @Override
    public CustomCompiler createCompiler() {
        checkForCompleteBuild();

        return new CustomCompilerFactory().produce(
            lexicalAnalyserFactory, 
            syntaxAnalyserFactory, 
            codeGeneratorFactory, 
            grammar, 
            whitespaceDelimiters, 
            stronglyReservedWords, 
            weaklyReservedWords, 
            dynamicTokenRegex,
            ruleConvertor
        );
    }
    
}
