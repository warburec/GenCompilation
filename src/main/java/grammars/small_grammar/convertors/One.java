package grammars.small_grammar.convertors;

import grammar_objects.*;
import grammars.small_grammar.SmallGrammar;
import helperObjects.NullableTuple;

public class One extends RuleConvertor {

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
    protected void setUpRuleConvertors(RuleOrganiser ruleOrganiser) {
        ruleOrganiser
        .setConversion(0, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) //E->E+B
        .setConversion(1, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) //E->E*B
        .setConversion(2, (elements) -> { return elements[0].getGeneration(); }) //E->B
        .setConversion(3, (elements) -> { return elements[0].getGeneration(); }) //B->0
        .setConversion(4, (elements) -> { return elements[0].getGeneration(); }); //B->1
    }
    
}
