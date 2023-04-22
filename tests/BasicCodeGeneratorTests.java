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
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor(sentence);
        String[] bookends = grammar.getGenerationBookends(sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends[0], bookends[1]);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, "Java");
        assertEquals(expectedCode, resultingCode);
    }

}
