package grammars.basic_identifier.convertors;

import code_generation.*;
import grammar_objects.*;
import grammars.basic_identifier.BasicIdentifierGrammar;
import helperObjects.NullableTuple;
import semantic_analysis.TypeChecker;

public class XToYToXSemantic extends RuleConvertor {

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
        TypeChecker typeChecker = new TypeChecker();

        ruleOrganiser
        .setConversion(0, (elements) -> { return elements[0].getGeneration(); }) //<statement list> := <statement>
        .setConversion(1, (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration(); }) //<statement list> := <statement list> <statement>
        .setConversion(2, (elements) -> {
            String identifierType = "";

            DynamicTokenGeneration identifier = (DynamicTokenGeneration)elements[0];
            if(!typeChecker.isDeclared(identifier)) {
                identifierType = "int "; //Note: Only works for "int" in this grammar
                typeChecker.declare(identifier);
            }

            return "\t\t" + identifierType + elements[0].getGeneration() + " " + 
            elements[1].getGeneration() + " " + elements[2].getGeneration() + " " + 
            elements[3].getGeneration() + " " + elements[4].getGeneration() + 
            elements[5].getGeneration() + "\n"; 
        })  //<statement> := identifier = <element> + <element>;
        .setConversion(3, (elements) -> { return elements[0].getGeneration(); }) //<element> := identifier
        .setConversion(4, (elements) -> { return elements[0].getGeneration(); }); //<element> := number
    }
    
}
