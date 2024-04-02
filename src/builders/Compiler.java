package builders;

import code_generation.CodeGenerator;
import grammar_objects.Token;
import lexical_analysis.LexicalAnalyser;
import syntax_analysis.SyntaxAnalyser;
import syntax_analysis.parsing.ParseFailedException;
import syntax_analysis.parsing.ParseState;

public class Compiler {
    protected LexicalAnalyser lexicalAnalyser;
    protected SyntaxAnalyser syntaxAnalyser;
    protected CodeGenerator codeGenerator;
    
    public Compiler(
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
