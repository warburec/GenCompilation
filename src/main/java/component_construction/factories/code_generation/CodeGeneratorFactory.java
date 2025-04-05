package component_construction.factories.code_generation;

import code_generation.CodeGenerator;
import grammar_objects.RuleConvertor;

public interface CodeGeneratorFactory {

    public CodeGenerator produceGenerator(RuleConvertor ruleConvertor);

}
