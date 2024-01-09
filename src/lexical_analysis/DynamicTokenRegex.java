package lexical_analysis;

import grammar_objects.Token;

public record DynamicTokenRegex(String regex, Class<? extends Token> classType, String grammaticalName) {}
