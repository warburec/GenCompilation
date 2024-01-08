package grammars.small_grammar.convertors;

import java.util.Map;

import code_generation.Generator;
import grammar_objects.*;
import grammars.small_grammar.SmallGrammar;
import helperObjects.NullableTuple;

public class EmptyReduce extends RuleConvertor {

    @Override
    protected Grammar setUpGrammar() {
        return new SmallGrammar();
    }

    @Override
    protected NullableTuple<String, String> setUpBookends() {
        return new NullableTuple<String,String>(
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tSystem.out.println(",

            ");\n" +
            "\t}\n" +
            "}"
        );
    }

    @Override
    protected void setUpRuleConvertors(Grammar grammar, Map<ProductionRule, Generator> ruleConversions) {
        ruleConversions.put(getRule(0), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E+B
        ruleConversions.put(getRule(1), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //E->E*B
        ruleConversions.put(getRule(2), (elements) -> { return elements[0].getGeneration(); }); //E->B
        ruleConversions.put(getRule(3), (elements) -> { return elements[0].getGeneration(); }); //B->0
        ruleConversions.put(getRule(4), (elements) -> { return elements[0].getGeneration(); }); //B->1
    }
    
}
