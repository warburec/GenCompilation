package builders.concrete_factories;

import builders.CodeGeneratorFactory;
import code_generation.*;
import grammar_objects.RuleConvertor;

public class BasicCodeGenFactory implements CodeGeneratorFactory {

    @Override
    public CodeGenerator produceGenerator(RuleConvertor ruleConvertor) {
        return new BasicCodeGenerator(ruleConvertor);
    }
    
}
