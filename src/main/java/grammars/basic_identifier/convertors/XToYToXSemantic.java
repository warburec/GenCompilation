package grammars.basic_identifier.convertors;

import java.util.*;

import code_generation.*;
import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import helper_objects.NullableTuple;
import semantic_analysis.TypeChecker;

public class XToYToXSemantic {

    public static RuleConvertor produce() {
        TypeChecker typeChecker = new TypeChecker();

        Grammar grammar = BasicIdentifierGrammar.produce();
        
        return new RuleConvertor(
            grammar,
            new HashMap<>(Map.of(
                grammar.getRule(0), (elements) -> { return elements[0].getGeneration(); },//<statement list> := <statement>
                grammar.getRule(1), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); },//<statement list> := <statement list> <statement>
                grammar.getRule(2), (elements) -> {
                    String identifierType = "";

                    DynamicTokenGeneration identifier = (DynamicTokenGeneration)elements[0];
                    if(!typeChecker.isDeclared(identifier)) {
                        identifierType = "int "; // Note: Only works for "int" in this grammar
                        typeChecker.declare(identifier);
                    }

                    return "\t\t" + identifierType + elements[0].getGeneration() + " " + 
                    elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
                    elements[3].getGeneration() + " " + elements[4].getGeneration() + 
                    elements[5].getGeneration() + "\n"; 
                }, //<statement> := identifier = <element> + <element>;
                grammar.getRule(3), (elements) -> { return elements[0].getGeneration(); },//<element> := identifier
                grammar.getRule(4), (elements) -> { return elements[0].getGeneration(); } //<element> := number
            )),
            new NullableTuple<String,String>(
                "public class TestGrammar {\n" +
                "\tpublic static void main(String[] args) {\n",

                "\t\tSystem.out.println(x);\n" +
                "\t}\n" +
                "}"
            )
        );
    }
    
}
