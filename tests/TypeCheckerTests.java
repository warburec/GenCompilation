package tests;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import code_generation.*;
import grammar_objects.ProductionRule;
import semantic_analysis.*;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.grammar_generators.*;

public class TypeCheckerTests {
    
    @Test
    public void XToYToXGeneration() {
        SemanticAnalyser analyser = new TypeChecker();
        BasicIdentifierGrammar grammar = new BasicIdentifierGrammar(analyser);
        String sentence = "XToYToXSemantic";
        String language = "Java";
        Map<ProductionRule, Generator> ruleConvertor = grammar.getSemanticRuleConvertor(sentence, language);
        String[] bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends[0], bookends[1]);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }
    
}
