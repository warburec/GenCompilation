package component_construction.factories.code_generation;

import code_generation.*;
import grammar_objects.RuleConvertor;

public class BasicCodeGenFactory implements CodeGeneratorFactory {

    @Override
    public CodeGenerator produceGenerator(RuleConvertor ruleConvertor) {
        return new BasicCodeGenerator(ruleConvertor);
    }
    
}
