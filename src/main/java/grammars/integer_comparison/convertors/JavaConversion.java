package grammars.integer_comparison.convertors;

import code_generation.*;
import grammar_objects.*;
import grammars.integer_comparison.IntegerCompGrammar;
import helper_objects.NullableTuple;
import semantic_analysis.TypeChecker;

public class JavaConversion extends RuleConvertor {

    @Override
    protected Grammar setUpGrammar() {
        return new IntegerCompGrammar();
    }

    @Override
    protected NullableTuple<String, String> setUpBookends() {
        return new NullableTuple<String,String>(
            "public class TestGrammar {\n" +
            "    public static void main(String[] args) {\n",

            "        System.out.println(x);\n" +
            "    }\n" +
            "}"
        );
    }

    @Override
    protected void setUpRuleConvertors(RuleOrganiser ruleOrganiser) {
        TypeChecker typeChecker = new TypeChecker();

        ruleOrganiser
        .setConversion(ROOT_RULE_INDEX, (elements) -> { return "        " + elements[0].getGeneration().stripTrailing().replaceAll("\n", "\n        ") + "\n"; })
        .setConversion(0, (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration() + "\n"; }) //<statement list> := <statement list> <statement>
        .setConversion(1, (elements) -> { return elements[0].getGeneration() + "\n"; }) //<statement list> := <statement>
        .setConversion(2, (elements) -> { return elements[0].getGeneration() + ";"; }) //<statement> := <assignment>;
        .setConversion(3, (elements) -> { return elements[0].getGeneration() + "\n"; }) //<statement> := <if statement>
        .setConversion(4, (elements) -> { 
            String generation =  "\nif(" + elements[2].getGeneration() + ") {\n    ";
            generation += elements[5].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }) //<if statement> := if(<condition>) {<statement list>}
        .setConversion(5, (elements) -> {
            String generation =  elements[0].getGeneration() + "\nelse {\n    ";
            generation += elements[3].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }) //<if statement> := <if statement> else {<statement list>}
        .setConversion(6, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) //<condition> := identifier <conditional operator> identifier
        .setConversion(7, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) //<condition> := identifier <conditional operator> numConstant
        .setConversion(8, (elements) -> { return elements[0].getGeneration(); }) //<conditional operator> := >
        .setConversion(9, (elements) -> { return elements[0].getGeneration(); }) //<conditional operator> := >=
        .setConversion(10, (elements) -> { return elements[0].getGeneration(); }) //<conditional operator> := ==
        .setConversion(11, (elements) -> { return elements[0].getGeneration(); }) //<conditional operator> := !=
        .setConversion(12, (elements) -> { return elements[0].getGeneration(); }) //<conditional operator> := <=
        .setConversion(13, (elements) -> { 
            String generation = "";

            DynamicTokenGeneration identifier = (DynamicTokenGeneration)elements[0];
            if(!typeChecker.isDeclared(identifier)) {
                typeChecker.declare(identifier);
                generation += "int "; //TODO: Handle types other than just fixed "int"
            }
            
            generation += elements[0].getGeneration();
            return generation + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); 
        }) //<assignment> := identifier = <expression>
        .setConversion(14, (elements) -> { return elements[0].getGeneration(); }) // <expression> := val identifier
        .setConversion(15, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> + identifier
        .setConversion(16, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> - identifier
        .setConversion(17, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> * identifier
        .setConversion(18, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> / identifier
        .setConversion(19, (elements) -> { return elements[0].getGeneration(); }) // <expression> := numConstant
        .setConversion(20, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> + numConstant
        .setConversion(21, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> - numConstant
        .setConversion(22, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }) // <expression> := <expression> * numConstant
        .setConversion(23, (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / numConstant
    }
    
}
