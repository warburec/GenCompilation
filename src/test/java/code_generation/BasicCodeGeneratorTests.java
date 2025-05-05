package code_generation;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code_generation.BasicCodeGenerator.IncompleteReductionException;
import grammar_objects.RuleConvertor;
import syntax_analysis.parsing.ParseState;
import test_aids.*;
import test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;
import test_aids.test_grammars.small_grammar.SmallTestGrammar;

public class BasicCodeGeneratorTests {
    
    @Test
    public void testGrammarJavaGeneration() {
        String language = "Java";
        String sentence = "1+0*1";
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getCodeGeneration(language + " " + sentence);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarCGeneration() {
        String language = "C";
        String sentence = "1+0*1";
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getCodeGeneration(language + " " + sentence);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarJavaMissingStateGeneration() {
        String language = "Java";
        String sentence = "1+0*1MissingReduction";
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        assertThrows(IncompleteReductionException.class, () -> codeGenerator.generate(rootParseState));
    }

    @Test
    public void testGrammarSigleDigitJavaGeneration() {
        String language = "Java";
        String sentence = "1";
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getCodeGeneration(language + " " + sentence);
        assertEquals(expectedCode, resultingCode);
    }

    @Test
    public void testGrammarEmptyReduceJavaGeneration() {
        String language = "Java";
        String sentence = "emptyReduce";
        TestGrammar grammar = new SmallTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        assertThrows(IncompleteReductionException.class, () -> codeGenerator.generate(rootParseState));
    }

    @Test
    public void XToYToXGeneration() {
        String language = "Java";
        String sentence = "XToYToX";
        TestGrammar grammar = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getCodeGeneration(language + " " + sentence);
        assertEquals(expectedCode, resultingCode);
    }
}
