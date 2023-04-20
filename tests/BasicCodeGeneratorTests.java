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
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor("1+0*1");
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot("1+0*1");

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode("1+0*1", "Java");
        assertEquals(expectedCode, resultingCode);
    }

}
