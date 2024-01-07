package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import code_generation.*;
import code_generation.BasicCodeGenerator.IncompleteReductionException;
import grammar_objects.ProductionRule;
import helperObjects.NullableTuple;
import syntax_analysis.parsing.ParseState;
import tests.test_aids.*;
import tests.test_aids.test_grammars.*;
import tests.test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;

public class BasicCodeGeneratorTests {
    
    @Test
    public void testGrammarJavaGeneration() {
        LR0TestGrammar grammar = new SmallTestGrammar();
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
        LR0TestGrammar grammar = new SmallTestGrammar();
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

    @Test
    public void testGrammarJavaMissingStateGeneration() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        String sentence = "1+0*1MissingReduction";
        String language = "Java";
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor(sentence, language);
        String[] bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends[0], bookends[1]);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        assertThrows(IncompleteReductionException.class, () -> codeGenerator.generate(rootParseState));
    }

    @Test
    public void testGrammarSigleDigitJavaGeneration() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        String sentence = "1";
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
    public void testGrammarEmptyReduceJavaGeneration() {
        LR0TestGrammar grammar = new SmallTestGrammar();
        String sentence = "emptyReduce";
        String language = "Java";
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor(sentence, language);
        String[] bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends[0], bookends[1]);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        assertThrows(IncompleteReductionException.class, () -> codeGenerator.generate(rootParseState));
    }

    @Test
    public void XToYToXGeneration() {
        TestGrammar grammar = new BasicIdentifierTestGrammar(GrammarType.LR0);
        String sentence = "XToYToX";
        String language = "Java";
        Map<ProductionRule, Generator> ruleConvertor = grammar.getRuleConvertor(sentence, language);
        NullableTuple<String, String> bookends = grammar.getGenerationBookends(sentence, language);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, bookends.value1(), bookends.value2());
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState); //TODO: FIx null being put in map instead of Generator

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }
}
