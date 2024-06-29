package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code_generation.*;
import code_generation.BasicCodeGenerator.IncompleteReductionException;
import grammar_objects.RuleConvertor;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.*;
import tests.test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;
import tests.test_aids.test_grammars.small_grammar.SmallTestGrammar;

public class BasicCodeGeneratorTests {
    
    @Test
    public void testGrammarJavaGeneration() {
        TestGrammar grammar = new SmallTestGrammar(GrammarType.LR0);
        String sentence = "1+0*1";
        String language = "Java";
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarCGeneration() {
        TestGrammar grammar = new SmallTestGrammar(GrammarType.LR0);
        String sentence = "1+0*1";
        String language = "C";
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarJavaMissingStateGeneration() {
        TestGrammar grammar = new SmallTestGrammar(GrammarType.LR0);
        String sentence = "1+0*1MissingReduction";
        String language = "Java";
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        assertThrows(IncompleteReductionException.class, () -> codeGenerator.generate(rootParseState));
    }

    @Test
    public void testGrammarSigleDigitJavaGeneration() {
        TestGrammar grammar = new SmallTestGrammar(GrammarType.LR0);
        String sentence = "1";
        String language = "Java";
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarEmptyReduceJavaGeneration() {
        TestGrammar grammar = new SmallTestGrammar(GrammarType.LR0);
        String sentence = "emptyReduce";
        String language = "Java";
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        assertThrows(IncompleteReductionException.class, () -> codeGenerator.generate(rootParseState));
    }

    @Test
    public void XToYToXGeneration() {
        TestGrammar grammar = new BasicIdentifierTestGrammar(GrammarType.LR0);
        String sentence = "XToYToX";
        String language = "Java";
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }
}
