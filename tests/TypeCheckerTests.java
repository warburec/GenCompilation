package tests;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import code_generation.*;
import grammar_objects.*;
import grammars.basic_identifier.convertors.XToXToYSemantic;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.*;
import tests.test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;

public class TypeCheckerTests {
    
    @Test
    public void XToYToXGeneration() {
        String language = "Java";
        String sentence = "XToYToXSemantic";
        TestGrammar grammar = new BasicIdentifierTestGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = new XToXToYSemantic();
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot("XToYToXSemantic");

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }
    
}
