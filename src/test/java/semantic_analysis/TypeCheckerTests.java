package semantic_analysis;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import code_generation.*;
import grammar_objects.*;
import syntax_analysis.parsing.ParseState;
import test_aids.*;
import test_aids.test_grammars.BasicIdentifierTestGrammar;

public class TypeCheckerTests {
    
    @Test
    public void XToYToXGeneration() {
        String language = "Java";
        String sentence = "XToYToXSemantic";
        TestGrammar grammar = new BasicIdentifierTestGrammar().getGrammar(GrammarType.LR0);
        RuleConvertor ruleConvertor = grammar.getRuleConvertor(language + " " + sentence);
        CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor);
        ParseState rootParseState = grammar.getParseRoot(sentence);

        String resultingCode = codeGenerator.generate(rootParseState);

        String expectedCode = grammar.getCodeGeneration(language + " " + sentence);
        assertEquals(expectedCode, resultingCode);
    }
    
}
