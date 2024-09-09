package code_generation;

import syntax_analysis.parsing.ParseState;

public interface CodeGenerator {
    public String generate(ParseState parseRoot);
}
