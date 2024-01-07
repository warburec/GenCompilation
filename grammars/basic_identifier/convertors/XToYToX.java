package grammars.basic_identifier.convertors;

import java.util.Map;

import code_generation.*;
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
    protected void setUpRuleConvertors(Grammar grammar, Map<ProductionRule, Generator> ruleConversions) { //TODO: Null being added instead of lambdas?
        ruleConversions.put(getRule(0), (elements) -> { return elements[0].getGeneration(); }); //<statement list> := <statement>
        ruleConversions.put(getRule(1), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); }); //<statement list> := <statement list> <statement>
        ruleConversions.put(getRule(2), (elements) -> {
            return "\t\t" + elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
            elements[3].getGeneration() + " " + elements[4].getGeneration() + elements[5].getGeneration() + "\n"; 
        });  //<statement> := identifier = <element> + <element>;
        ruleConversions.put(getRule(3), (elements) -> { return ((IdentifierGeneration)elements[0]).getGeneration(); }); //<element> := identifier
        ruleConversions.put(getRule(4), (elements) -> { return ((LiteralGeneration)elements[0]).getGeneration(); }); //<element> := number
    }
    
}
