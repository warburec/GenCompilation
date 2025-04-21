package grammars.integer_comparison.convertors;

import java.util.*;

import code_generation.*;
import grammar_objects.*;
import grammars.integer_comparison.IntegerCompGrammar;
import helper_objects.NullableTuple;
import semantic_analysis.TypeChecker;

public class JavaConversion  {

    public static RuleConvertor produce() {
        TypeChecker typeChecker = new TypeChecker();

        Grammar grammar = IntegerCompGrammar.produce();

        Map<ProductionRule, Generator> convertors = new HashMap<>();
        convertors.put(RuleConvertor.ROOT_RULE, (elements) -> { return "        " + elements[0].getGeneration().stripTrailing().replaceAll("\n", "\n        ") + "\n"; });
        convertors.put(grammar.getRule(0), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration() + "\n"; }); //<statement list> := <statement list> <statement>
        convertors.put(grammar.getRule(1), (elements) -> { return elements[0].getGeneration() + "\n"; }); //<statement list> := <statement>
        convertors.put(grammar.getRule(2), (elements) -> { return elements[0].getGeneration() + ";"; }); //<statement> := <assignment>;
        convertors.put(grammar.getRule(3), (elements) -> { return elements[0].getGeneration() + "\n"; }); //<statement> := <if statement>
        convertors.put(grammar.getRule(4), (elements) -> { 
            String generation =  "\nif(" + elements[2].getGeneration() + ") {\n    ";
            generation += elements[5].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }); //<if statement> := if(<condition>) {<statement list>}
        convertors.put(grammar.getRule(5), (elements) -> {
            String generation =  elements[0].getGeneration() + "\nelse {\n    ";
            generation += elements[3].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }); //<if statement> := <if statement> else {<statement list>}
        convertors.put(grammar.getRule(6), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //<condition> := identifier <conditional operator> identifier
        convertors.put(grammar.getRule(7), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //<condition> := identifier <conditional operator> numConstant
        convertors.put(grammar.getRule(8), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := >
        convertors.put(grammar.getRule(9), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := >=
        convertors.put(grammar.getRule(10), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := ==
        convertors.put(grammar.getRule(11), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := !=
        convertors.put(grammar.getRule(12), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := <=
        convertors.put(grammar.getRule(13), (elements) -> { 
            String generation = "";

            DynamicTokenGeneration identifier = (DynamicTokenGeneration)elements[0];
            if(!typeChecker.isDeclared(identifier)) {
                typeChecker.declare(identifier);
                generation += "int "; //TODO: Handle types other than just fixed "int"
            }
            
            generation += elements[0].getGeneration();
            return generation + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); 
        }); //<assignment> := identifier = <expression>
        convertors.put(grammar.getRule(14), (elements) -> { return elements[0].getGeneration(); }); // <expression> := val identifier
        convertors.put(grammar.getRule(15), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> + identifier
        convertors.put(grammar.getRule(16), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> - identifier
        convertors.put(grammar.getRule(17), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> * identifier
        convertors.put(grammar.getRule(18), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / identifier
        convertors.put(grammar.getRule(19), (elements) -> { return elements[0].getGeneration(); }); // <expression> := numConstant
        convertors.put(grammar.getRule(20), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> + numConstant
        convertors.put(grammar.getRule(21), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> - numConstant
        convertors.put(grammar.getRule(22), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> * numConstant
        convertors.put(grammar.getRule(23), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / numConstant
        
        return new RuleConvertor(
            grammar,
            convertors,
            new NullableTuple<String,String>(
                "public class TestGrammar {\n" +
                "    public static void main(String[] args) {\n",

                "        System.out.println(x);\n" +
                "    }\n" +
                "}"
            )
        );
    }
    
}
