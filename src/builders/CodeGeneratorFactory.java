package builders;

import code_generation.CodeGenerator;
import grammar_objects.RuleConvertor;

public abstract class CodeGeneratorFactory {

    public abstract CodeGenerator produceGenerator(RuleConvertor ruleConvertor);

}
