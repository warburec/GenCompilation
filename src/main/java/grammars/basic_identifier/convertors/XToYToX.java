package grammars.basic_identifier.convertors;

import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import helperObjects.NullableTuple;

public class XToYToX extends RuleConvertor {

    @Override
    protected Grammar setUpGrammar() {
        return new BasicIdentifierGrammar();
    }

    @Override
    protected NullableTuple<String, String> setUpBookends() {
        return new NullableTuple<String,String>(
            "public class TestGrammar {\n" +
            "\tpublic static void main(String[] args) {\n",

            "\t\tSystem.out.println(x);\n" +
            "\t}\n" +
            "}"
        );
    }

    @Override
    protected void setUpRuleConvertors(RuleOrganiser ruleOrganiser) {
        ruleOrganiser
        .setConversion(0, (elements) -> { return elements[0].getGeneration(); }) //<statement list> := <statement>
        .setConversion(1, (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); }) //<statement list> := <statement list> <statement>
        .setConversion(2, (elements) -> {
            return "\t\t" + elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
            elements[3].getGeneration() + " " + elements[4].getGeneration() + elements[5].getGeneration() + "\n"; 
        })  //<statement> := identifier = <element> + <element>;
        .setConversion(3, (elements) -> { return elements[0].getGeneration(); }) //<element> := identifier
        .setConversion(4, (elements) -> { return elements[0].getGeneration(); }); //<element> := number
    }
    
}
