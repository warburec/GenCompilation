package grammars.right_recursive.convertors;

import java.util.*;

import code_generation.Generator;
import grammar_objects.*;
import grammars.right_recursive.RightRecursive;

public class AutoConfig {
    
    public static RuleConvertor produce() {
        Grammar grammar = RightRecursive.produce();
        Generator defaultGeneration = (elements) -> { return elements[0].getGeneration(); };

        return new RuleConvertor(
            grammar,
            new HashMap<>(Map.of(
                grammar.getRule(0), elements -> { // <SectionBlock> -> SECTION Identifier { <PropertyList> }
                    String id = elements[1].getGeneration();
                    return "/* Auto-Generated Config Block: " + id + " */\n" + elements[3].getGeneration();
                },
                grammar.getRule(1), defaultGeneration,// <PropertyList> -> <PropertyListHead> <Property>
                grammar.getRule(2), defaultGeneration,// <PropertyListHead> -> <PropertyListHead> <Property> 
                grammar.getRule(3), defaultGeneration,// <PropertyListHead> -> epsilon
                grammar.getRule(4), elements -> { // <Property> -> Identifier = Literal ;
                    String key = elements[0].getGeneration();
                    String val = elements[2].getGeneration();
                    return "System.setProperty(\"" + key + "\", " + val + ");\n";
                }
            ))
        );
    }
    
}
