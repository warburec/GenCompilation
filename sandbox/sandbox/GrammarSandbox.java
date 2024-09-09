package sandbox;

import org.junit.jupiter.api.Test;

import builders.CompilerBuilder;
import builders.bundles.GrammarBundle;
import builders.concrete_factories.BasicCodeGenFactory;
import builders.concrete_factories.CLR1ParserFactory;
import builders.concrete_factories.GeneralLexicalAnalyserFactory;
import grammar_objects.Grammar;
import grammar_objects.RuleConvertor;
import grammars.basic_identifier.BasicIdentifierGrammar;
import grammars.basic_identifier.convertors.XToYToXSemantic;
import lexical_analysis.DynamicTokenRegex;
import syntax_analysis.parsing.ParseFailedException;

public class GrammarSandbox {
    
    // @Test
    // public void integerComp() {
    //     String language = "Java";
    //     String sentence = "JavaConversion";

    //     IntegerCompGrammar grammar = new IntegerCompGrammar();
    //     GrammarParts grammarParts = grammar.getParts();
    //     SyntaxAnalyser syntaxAnalyser = new LR0Parser(
    //         grammarParts.tokens(), 
    //         grammarParts.nonTerminals(),
    //         grammarParts.productionRules(),
    //         grammarParts.sentinal());

    //     ParseState parseRoot = null;
    //     try {
    //         /*
    //          * x = 1;
    //          * y = 2;
    //          * if(x <= y) {
    //          *  x = y * y;
    //          * }
    //          * else {
    //          *  x = -10;
    //          * }
    //          * y = 5 + x;
    //          * x = x * y;
    //          */
    //         parseRoot = syntaxAnalyser.analyse(new Token[] {
    //             new Identifier("identifier", "int", "x"),
    //             new Token("="),
    //             new Literal("numConstant", "1"),
    //             new Token(";"),
    //             new Identifier("identifier", "int", "y"),
    //             new Token("="),
    //             new Literal("numConstant", "2"),
    //             new Token(";"),
    //             new Token("if"),
    //             new Token("("),
    //             new Identifier("identifier", "int", "x"),
    //             new Token("<="),
    //             new Identifier("identifier", "int", "y"),
    //             new Token(")"),
    //             new Token("{"),
    //             new Identifier("identifier", "int", "x"),
    //             new Token("="),
    //             new Identifier("identifier", "int", "y"),
    //             new Token("*"),
    //             new Identifier("identifier", "int", "y"),
    //             new Token(";"),
    //             new Token("}"),
    //             new Token("else"),
    //             new Token("{"),
    //             new Identifier("identifier", "int", "x"),
    //             new Token("="),
    //             new Literal("numConstant", "-10"),
    //             new Token(";"),
    //             new Token("}"),
    //             new Identifier("identifier", "int", "y"),
    //             new Token("="),
    //             new Literal("numConstant", "5"),
    //             new Token("+"),
    //             new Identifier("identifier", "int", "x"),
    //             new Token(";"),
    //             new Identifier("identifier", "int", "x"),
    //             new Token("="),
    //             new Identifier("identifier", "int", "x"),
    //             new Token("*"),
    //             new Identifier("identifier", "int", "y"),
    //             new Token(";")
    //         });
    //     } catch (ParseFailedException e) {
    //         e.getCause().printStackTrace();
    //         System.out.println(e.getMessage());
    //     }

    //     if(parseRoot == null) { return; }

    //     String[] bookends = grammar.getGenerationBookends(sentence, language);
    //     CodeGenerator codeGenerator = new BasicCodeGenerator(
    //         grammar.getSemanticRuleConvertor(sentence, language),
    //         bookends[0], 
    //         bookends[1]);
        
    //     String output = codeGenerator.generate(parseRoot);

    //     System.out.println(output);
    // }


    // // Language allows computation to output an amaount of slashes
    // // // Grammar //
    // // assignment ::= identifier = addition
    // // expression ::= multiplication
    // // expression ::= addition
    // // expression ::= identifier
    // // expression ::= number
    // // multiplication ::= [ expression * expression ]
    // // addition ::= ( expression + expression )
    // // output ::= < output-expression >
    // // output-expression ::= output-value
    // // output-expression ::= output-expression output-value
    // // output-value ::= identifier
    // // output-value ::= number
    // // statement ::= assignment
    // // statement ::= output
    // // statement-list ::= statement
    // // statement-list ::= statement-list statement
    // @Test
    // public void test() {
    //     String[] delims = new String[] {
    //         " ",
    //         "\n"
    //     };
    //     String[] strongWords = new String[] {
    //         "[",
    //         "]",
    //         "(",
    //         ")",
    //         "+",
    //         "*",
    //         "<",
    //         ">",
    //         "="
    //     };
    //     String[] weakWords = new String[] {

    //     };
    //     Map<String,NotEmptyTuple<String,String>> dynamicTokenRegex = new HashMap<String,NotEmptyTuple<String,String>>();
    //     dynamicTokenRegex.put("[a-zA-Z]+", new NotEmptyTuple<String,String>("Identifier", "identifier"));
    //     dynamicTokenRegex.put("[0-9]+", new NotEmptyTuple<String,String>("Literal", "number"));

    //     LexicalAnalyser lexicalAnalyser = new GeneralLexicalAnalyser(delims, strongWords, weakWords, dynamicTokenRegex);


    //     String sentence = 
    //         "a = [(1 + 3) * 3]\n" +
    //         "b = [2 * 5]\n" +
    //         "<a b>\n" +
    //         "<3>";

    //     ProductionRule[] productionRules = new ProductionRule[] {
    //         new ProductionRule(
    //             new NonTerminal("assignment"), 
    //             new LexicalElement[] {
    //                 new Identifier("identifier"),
    //                 new Token("="),
    //                 new NonTerminal("expression")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("expression"), 
    //             new LexicalElement[] {
    //                 new NonTerminal("multiplication")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("expression"), 
    //             new LexicalElement[] {
    //                 new NonTerminal("addition")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("expression"), 
    //             new LexicalElement[] {
    //                 new Identifier("identifier")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("expression"), 
    //             new LexicalElement[] {
    //                 new Literal("number")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("multiplication"), 
    //             new LexicalElement[] {
    //                 new Token("["),
    //                 new NonTerminal("expression"),
    //                 new Token("*"),
    //                 new NonTerminal("expression"),
    //                 new Token("]"),
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("addition"), 
    //             new LexicalElement[] {
    //                 new Token("("),
    //                 new NonTerminal("expression"),
    //                 new Token("+"),
    //                 new NonTerminal("expression"),
    //                 new Token(")"),
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("output"), 
    //             new LexicalElement[] {
    //                 new Token("<"),
    //                 new NonTerminal("output expression"),
    //                 new Token(">"),
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("output expression"), 
    //             new LexicalElement[] {
    //                 new NonTerminal("output value")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("output expression"), 
    //             new LexicalElement[] {
    //                 new NonTerminal("output expression"),
    //                 new NonTerminal("output value"),
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("output value"),
    //             new LexicalElement[] {
    //                 new Identifier("identifier")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("output value"),
    //             new LexicalElement[] {
    //                 new Literal("number")
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("statement"),
    //             new LexicalElement[] {
    //                 new NonTerminal("assignment"), 
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("statement"),
    //             new LexicalElement[] {
    //                 new NonTerminal("output"),
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("statement list"),
    //             new LexicalElement[] {
    //                 new NonTerminal("statement"),
    //             }
    //         ),
    //         new ProductionRule(
    //             new NonTerminal("statement list"),
    //             new LexicalElement[] {
    //                 new NonTerminal("statement list"),
    //                 new NonTerminal("statement"),
    //             }
    //         ),
    //     };
    //     NonTerminal sentinel = new NonTerminal("statement list");

    //     SyntaxAnalyser syntaxAnalyser = new LR0Parser(productionRules, sentinel);

    //     ParseState analysedSyntaxStates;
    //     try {
    //         Token[] generatedTokens = lexicalAnalyser.analyse(sentence);
    //         analysedSyntaxStates = syntaxAnalyser.analyse(generatedTokens);
    //     } catch (ParseFailedException e) {
    //         throw new RuntimeException(e);
    //     }


    //     Map<String, Integer> identifierMap = new HashMap<>();

    //     Map<ProductionRule,Generator> ruleConvertor = new HashMap<ProductionRule,Generator>();
    //     ruleConvertor.put(productionRules[0], (CodeElement[] elements) -> {
    //         identifierMap.put(elements[0].getGeneration(), Integer.parseInt(elements[2].getGeneration()));
    //         return "";
    //     });
    //     ruleConvertor.put(productionRules[1], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[2], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[3], (CodeElement[] elements) -> {
    //         return identifierMap.get(elements[0].getGeneration()).toString();
    //     });
    //     ruleConvertor.put(productionRules[4], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[5], (CodeElement[] elements) -> {
    //         return String.valueOf(Integer.parseInt(elements[1].getGeneration()) * Integer.parseInt(elements[3].getGeneration()));
    //     });
    //     ruleConvertor.put(productionRules[6], (CodeElement[] elements) -> {
    //         return String.valueOf(Integer.parseInt(elements[1].getGeneration()) + Integer.parseInt(elements[3].getGeneration()));
    //     });
    //     ruleConvertor.put(productionRules[7], (CodeElement[] elements) -> {
    //         return elements[1].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[8], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[9], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration() + " " + elements[1].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[10], (CodeElement[] elements) -> {
    //         int numOfSlashes = identifierMap.get(elements[0].getGeneration());
    //         return "\\".repeat(numOfSlashes);
    //     });
    //     ruleConvertor.put(productionRules[11], (CodeElement[] elements) -> {
    //         int numOfSlashes = Integer.parseInt(elements[0].getGeneration());
    //         return "\\".repeat(numOfSlashes);
    //     });
    //     ruleConvertor.put(productionRules[12], (CodeElement[] elements) -> {
    //         return "";
    //     });
    //     ruleConvertor.put(productionRules[13], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration() + "\n";
    //     });
    //     ruleConvertor.put(productionRules[14], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration();
    //     });
    //     ruleConvertor.put(productionRules[15], (CodeElement[] elements) -> {
    //         return elements[0].getGeneration() + elements[1].getGeneration();
    //     });

    //     CodeGenerator codeGenerator = new BasicCodeGenerator(ruleConvertor, "", "");

    //     String output = codeGenerator.generate(analysedSyntaxStates);

    //     System.out.println(output);
    // }

    // private class BasicIdentBundle implements GrammarBundle {

    //     @Override
    //     public Grammar getGrammar() { return new BasicIdentifierGrammar(); }

    //     @Override
    //     public RuleConvertor getRuleConvertor() { return new XToYToXSemantic(); }

    //     @Override
    //     public String[] getWhitespaceDelimiters() { 
    //         return new String[] {"\n", "\r", " ", "\t"};
    //     }
            
    //     @Override
    //     public String[] getStronglyReservedWords() {
    //         return new String[] {"+", "=", ";"};
    //     }

    //     @Override
    //     public String[] getWeaklyReservedWords() {
    //         return new String[] {};
    //     }

    //     @Override
    //     public DynamicTokenRegex[] getDynamicTokenRegex() {
    //         return new DynamicTokenRegex[] {
    //             new DynamicTokenRegex("[A-Za-z]+", "identifier"),
    //             new DynamicTokenRegex("[0-9]+|[0-9]+.[0-9]+", "number")
    //         };
    //     }

    // }

    // private class DefaultCompilerBuilder extends CompilerBuilder {
    //     public DefaultCompilerBuilder() {
    //         setComponents(
    //             new GeneralLexicalAnalyserFactory(),
    //             new CLR1ParserFactory(),
    //             new BasicCodeGenFactory(),
    //             new BasicIdentBundle()
    //         );
    //     }
    // }

    // @Test
    // public void compilerBuilder() throws ParseFailedException {
    //     String input = """
    //     x = 1 + 2;
    //     y = x + 3;
    //     x = y + 0;
    //     """;

    //     System.out.println(
    //         new DefaultCompilerBuilder()
    //         .createCompiler()
    //         .compile(input)
    //     );
    // }
}
