package grammars.integer_comparison.convertors;

import java.util.Map;

import code_generation.*;
import grammar_objects.*;
import grammars.integer_comparison.IntegerCompGrammar;
import helperObjects.NullableTuple;
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
    protected void setUpRuleConvertors(Grammar grammar, Map<ProductionRule, Generator> ruleConversions) {
        TypeChecker typeChecker = new TypeChecker();

        ruleConversions.put(BasicCodeGenerator.ROOT_RULE, (elements) -> { return "        " + elements[0].getGeneration().stripTrailing().replaceAll("\n", "\n        ") + "\n"; });
        ruleConversions.put(getRule(0), (elements) -> { return elements[0].getGeneration() + elements[1].getGeneration() + "\n"; }); //<statement list> := <statement list> <statement>
        ruleConversions.put(getRule(1), (elements) -> { return elements[0].getGeneration() + "\n"; }); //<statement list> := <statement>
        ruleConversions.put(getRule(2), (elements) -> { return elements[0].getGeneration() + ";"; }); //<statement> := <assignment>;
        ruleConversions.put(getRule(3), (elements) -> { return elements[0].getGeneration() + "\n"; }); //<statement> := <if statement>
        ruleConversions.put(getRule(4), (elements) -> { 
            String generation =  "\nif(" + elements[2].getGeneration() + ") {\n    ";
            generation += elements[5].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }); //<if statement> := if(<condition>) {<statement list>}
        ruleConversions.put(getRule(5), (elements) -> {
            String generation =  elements[0].getGeneration() + "\nelse {\n    ";
            generation += elements[3].getGeneration().stripTrailing().replaceAll("\n", "\n    ");
            return generation + "\n}";
        }); //<if statement> := <if statement> else {<statement list>}
        ruleConversions.put(getRule(6), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //<condition> := identifier <conditional operator> identifier
        ruleConversions.put(getRule(7), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); //<condition> := identifier <conditional operator> numConstant
        ruleConversions.put(getRule(8), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := >
        ruleConversions.put(getRule(9), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := >=
        ruleConversions.put(getRule(10), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := ==
        ruleConversions.put(getRule(11), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := !=
        ruleConversions.put(getRule(12), (elements) -> { return elements[0].getGeneration(); }); //<conditional operator> := <=
        ruleConversions.put(getRule(13), (elements) -> { 
            String generation = "";

            IdentifierGeneration identifier = (IdentifierGeneration)elements[0];
            if(!typeChecker.isDeclared(identifier)) {
                typeChecker.declare(identifier);
                generation += identifier.getType() + " ";
            }
            
            generation += elements[0].getGeneration();
            return generation + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); 
        }); //<assignment> := identifier = <expression>
        ruleConversions.put(getRule(14), (elements) -> { return elements[0].getGeneration(); }); // <expression> := val identifier
        ruleConversions.put(getRule(15), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> + identifier
        ruleConversions.put(getRule(16), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> - identifier
        ruleConversions.put(getRule(17), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> * identifier
        ruleConversions.put(getRule(18), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / identifier
        ruleConversions.put(getRule(19), (elements) -> { return elements[0].getGeneration(); }); // <expression> := numConstant
        ruleConversions.put(getRule(20), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> + numConstant
        ruleConversions.put(getRule(21), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> - numConstant
        ruleConversions.put(getRule(22), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> * numConstant
        ruleConversions.put(getRule(23), (elements) -> { return elements[0].getGeneration() + " " + elements[1].getGeneration() + " " + elements[2].getGeneration(); }); // <expression> := <expression> / numConstant
    }
    
}
