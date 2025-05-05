package grammars.small_grammar.convertors;

import java.util.*;

import grammar_objects.*;
import grammars.small_grammar.SmallGrammar;
import helper_objects.NullableTuple;

public class One {

    public static RuleConvertor produce() {
        Grammar grammar = SmallGrammar.produce();
        
        return new RuleConvertor(
            grammar,
            new HashMap<>(Map.of(
                grammar.getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }, //E->E+B
                grammar.getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }, //E->E*B
                grammar.getRule(2), (elements) -> { return elements[0].getGeneration(); }, //E->B
                grammar.getRule(3), (elements) -> { return elements[0].getGeneration(); }, //B->0
                grammar.getRule(4), (elements) -> { return elements[0].getGeneration(); } //B->1
            )),
            new NullableTuple<String,String>(
                "public class TestGrammar {\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tSystem.out.println(",

                ");\n" +
                "\t}\n" +
                "}"
            )
        );
    }
    
}
