package component_construction.custom_components;

import code_generation.CodeGenerator;
import component_construction.Compiler;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

/**
 * A compiler that can be built with user-definable inner components
 */
public class CustomCompiler implements Compiler {
    protected LexicalAnalyser lexicalAnalyser;
    protected SyntaxAnalyser syntaxAnalyser;
    protected CodeGenerator codeGenerator;
    
    public CustomCompiler(
        LexicalAnalyser lexicalAnalyser,
        SyntaxAnalyser syntaxAnalyser,
        CodeGenerator codeGenerator
    ) {
        this.lexicalAnalyser = lexicalAnalyser;
        this.syntaxAnalyser = syntaxAnalyser;
        this.codeGenerator = codeGenerator;
    }

    public String compile(String input) throws ParseFailedException {
        Token[] tokens = lexicalAnalyser.analyse(input);
        ParseState parseRoot = syntaxAnalyser.analyse(tokens);
        return codeGenerator.generate(parseRoot);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomCompiler)) return false;

        CustomCompiler other = (CustomCompiler)obj;
        if (!this.lexicalAnalyser.equals(other.lexicalAnalyser)) return false;
        if (!this.syntaxAnalyser.equals(other.syntaxAnalyser)) return false;
        if (!this.codeGenerator.equals(other.codeGenerator)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 31 
            * lexicalAnalyser.hashCode()
            * syntaxAnalyser.hashCode()
            * codeGenerator.hashCode();
    }
}