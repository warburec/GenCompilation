package grammars.basic_identifier.convertors;

import java.util.Map;

import code_generation.Generator;
import code_generation.IdentifierGeneration;
import code_generation.LiteralGeneration;
import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import helperObjects.NullableTuple;
import semantic_analysis.TypeChecker;

public class XToXToYSemantic extends RuleConvertor {

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
    protected void setUpRuleConvertors(Grammar grammar, Map<ProductionRule, Generator> ruleConversions) {
        TypeChecker typeChecker = new TypeChecker();

        ruleConversions.put(getRule(0), (elements) -> { return elements[0].getGeneration(); }); //<statement list> := <statement>
        ruleConversions.put(getRule(1), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); }); //<statement list> := <statement list> <statement>
        ruleConversions.put(getRule(2), (elements) -> {
            String identifierType = "";
            IdentifierGeneration identifier = (IdentifierGeneration)elements[0];
            if(!typeChecker.isDeclared(identifier)) {
                identifierType = identifier.getType() + " ";
                typeChecker.declare(identifier);
            }

            return "\t\t" + identifierType + elements[0].getGeneration() + " " + 
            elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
            elements[3].getGeneration() + " " + elements[4].getGeneration() + 
            elements[5].getGeneration() + "\n"; 
        });  //<statement> := identifier = <element> + <element>;
        ruleConversions.put(getRule(3), (elements) -> { return ((IdentifierGeneration)elements[0]).getGeneration(); }); //<element> := identifier
        ruleConversions.put(getRule(4), (elements) -> { return ((LiteralGeneration)elements[0]).getGeneration(); }); //<element> := number
    }
    
}
