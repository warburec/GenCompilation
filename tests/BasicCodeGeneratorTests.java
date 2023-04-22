package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import code_generation.*;
import grammar_objects.ProductionRule;
import syntax_analysis.parsing.ParseState;
import tests.testAids.GrammarGenerators.TestGrammar;

public class BasicCodeGeneratorTests {
    
    @Test
    public void testGrammarJavaGeneration() {
        TestGrammar grammar = new TestGrammar();
        String sentence = "1+0*1";
        String language = "Java";
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor(sentence, language);
        String[] bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends[0], bookends[1]);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarCGeneration() {
        TestGrammar grammar = new TestGrammar();
        String sentence = "1+0*1";
        String language = "C";
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor(sentence, language);
        String[] bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends[0], bookends[1]);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }

}
