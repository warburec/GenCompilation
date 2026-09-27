package test_aids.storage_entities.custom_components.factories;

import code_generation.CodeGenerator;
import component_construction.factories.code_generation.CodeGeneratorFactory;
import grammar_objects.RuleConvertor;
import test_aids.storage_entities.custom_components.TestStorableCustomCodeGenerator;

public class TestStorableCustomCodeGeneratorFactory implements CodeGeneratorFactory {

    @Override
    public CodeGenerator produceGenerator(RuleConvertor ruleConvertor) {
        return new TestStorableCustomCodeGenerator();
    }
    
}
