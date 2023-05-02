package sandbox;

import org.junit.Test;

import code_generation.*;
import grammar_objects.*;
import semantic_analysis.TypeChecker;
import syntax_analysis.*;
import syntax_analysis.grammar_structure_creation.UnsupportedShiftException;
import syntax_analysis.parsing.ParseFailedException;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.GrammarParts;
import tests.test_aids.grammar_generators.*;

public class GrammarSandbox {
    
    @Test
    public void integerComp() {
        String language = "Java";
        String sentence = "JavaConversion";

        TypeChecker typeChecker = new TypeChecker();

        IntegerCompGrammar grammar = new IntegerCompGrammar(typeChecker);
        GrammarParts grammarParts = grammar.getParts();
        SyntaxAnalyser syntaxAnalyser = new LR0Parser(
            grammarParts.tokens(), 
            grammarParts.nonTerminals(),
            grammarParts.productionRules(),
            grammarParts.sentinal());

        ParseState parseRoot = null;
        try {
            /*
             * x = 1;
             * y = 2;
             * if(x <= y) {
             *  x = y * y;
             * }
             * else {
             *  x = -10;
             * }
             * y = 5 + x;
             * x = x * y;
             */
            parseRoot = syntaxAnalyser.analyse(new Token[] {
                new Identifier("identifier", "int", "x"),
                new Token("="),
                new Literal("numConstant", "1"),
                new Token(";"),
                new Identifier("identifier", "int", "y"),
                new Token("="),
                new Literal("numConstant", "2"),
                new Token(";"),
                new Token("if"),
                new Token("("),
                new Identifier("identifier", "int", "x"),
                new Token("<="),
                new Identifier("identifier", "int", "y"),
                new Token(")"),
                new Token("{"),
                new Identifier("identifier", "int", "x"),
                new Token("="),
                new Identifier("identifier", "int", "y"),
                new Token("*"),
                new Identifier("identifier", "int", "y"),
                new Token(";"),
                new Token("}"),
                new Token("else"),
                new Token("{"),
                new Identifier("identifier", "int", "x"),
                new Token("="),
                new Literal("numConstant", "-10"),
                new Token(";"),
                new Token("}"),
                new Identifier("identifier", "int", "y"),
                new Token("="),
                new Literal("numConstant", "5"),
                new Token("+"),
                new Identifier("identifier", "int", "x"),
                new Token(";"),
                new Identifier("identifier", "int", "x"),
                new Token("="),
                new Identifier("identifier", "int", "x"),
                new Token("*"),
                new Identifier("identifier", "int", "y"),
                new Token(";")
            });
        } catch (ParseFailedException e) {
            ((UnsupportedShiftException)e.getCause()).printStackTrace();
            System.out.println(e.getMessage());
        }

        if(parseRoot == null) { return; }

        String[] bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(
            grammar.getSemanticRuleConvertor(sentence, language),
            bookends[0], 
            bookends[1]);
        
        String output = codeGenerator.generate(parseRoot);

        System.out.println(output);
    }
}
