package component_construction.custom_components;

import code_generation.CodeGenerator;
import component_construction.Compiler;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.*;

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
}