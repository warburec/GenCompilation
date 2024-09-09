import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code_generation.*;
import grammar_objects.*;
import grammars.basic_identifier.convertors.XToYToXSemantic;
import syntax_analysis.parsing.ParseState;
import test_aids.*;
import test_aids.test_grammars.basic_identifier.BasicIdentifierTestGrammar;

public class TypeCheckerTests {
    
    @Test
    public void XToYToXGeneration() {
        String language = "Java";
        String sentence = "XToYToXSemantic";
        TestGrammar grammar = new BasicIdentifierTestGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = new XToYToXSemantic();
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot("XToYToXSemantic");

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getGeneratedCode(sentence, language);
        assertEquals(expectedCode, resultingCode);
    }
    
}
