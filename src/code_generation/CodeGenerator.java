package code_generation;

import syntax_analysis.parsing.ParseState;

public interface CodeGenerator {
    public String generate(ParseState parseRoot);
    public boolean isDeclared(IdentifierGeneration identifier);
    public void setDeclared(IdentifierGeneration identfier);
}
