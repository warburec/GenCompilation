package tests;

import org.junit.Test;

import builders.CompilerBuilder;
import builders.Compiler;

public class CompilerBuilderTests {
    
    @Test
    public void basicCompilerBuilder() {
        CompilerBuilder compilerBuilder = new CompilerBuilder();

        Compiler compiler = compilerBuilder.createCompiler();
    }
}
