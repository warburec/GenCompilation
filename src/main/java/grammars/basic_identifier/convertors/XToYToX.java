package grammars.basic_identifier.convertors;

import java.util.*;

import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import helper_objects.NullableTuple;

public class XToYToX {

    public static RuleConvertor produce() {
        Grammar grammar = BasicIdentifierGrammar.produce();
        
        return new RuleConvertor(
            grammar,
            new HashMap<>(Map.of(
                grammar.getRule(0), (elements) -> { return elements[0].getGeneration(); }, //<statement list> := <statement>
                grammar.getRule(1), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); }, //<statement list> := <statement list> <statement>
                grammar.getRule(2), (elements) -> {
                    return "\t\t" + elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
                    elements[3].getGeneration() + " " + elements[4].getGeneration() + elements[5].getGeneration() + "\n"; 
                },  //<statement> := identifier = <element> + <element>;
                grammar.getRule(3), (elements) -> { return elements[0].getGeneration(); }, //<element> := identifier
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
