package builders;

import java.util.*;

import builders.bundles.GrammarBundle;
import grammar_objects.*;
import lexical_analysis.*;
import syntax_analysis.SyntaxAnalyser;
import code_generation.CodeGenerator;

public class CompilerBuilder {
    protected LexicalAnalyserFactory lexicalAnalyserFactory;
    protected SyntaxAnalyserFactory syntaxAnalyserFactory;
    protected CodeGeneratorFactory codeGeneratorFactory;
    
    protected Grammar grammar;
    protected RuleConvertor ruleConvertor;

    protected String[] whitespaceDelimiters;
    protected String[] stronglyReservedWords;
    protected String[] weaklyReservedWords;
    protected DynamicTokenRegex[] dynamicTokenRegex;

    /**
     * A shortcut setter method for all components necessary for building a compiler
     * @param lexicalAnalyserFactory
     * @param syntaxAnalyserFactory
     * @param codeGeneratorFactory
     * @param grammarBundle
     */
    public CompilerBuilder setComponents(
        LexicalAnalyserFactory lexicalAnalyserFactory,
        SyntaxAnalyserFactory syntaxAnalyserFactory,
        CodeGeneratorFactory codeGeneratorFactory,
        GrammarBundle grammarBundle
    ) {
        return setComponents(
            lexicalAnalyserFactory, 
            syntaxAnalyserFactory, 
            codeGeneratorFactory, 
            grammarBundle.getGrammar(),
            grammarBundle.getRuleConvertor(),
            grammarBundle.getWhitespaceDelimiters(),
            grammarBundle.getStronglyReservedWords(),
            grammarBundle.getWeaklyReservedWords(),
            grammarBundle.getDynamicTokenRegex()
        );
    }
    
    /**
     * A shortcut setter method for all components necessary for building a compiler
     * @param lexicalAnalyserFactory
     * @param syntaxAnalyserFactory
     * @param codeGeneratorFactory
     * @param grammar
     * @param ruleConvertor
     * @param whitespaceDelimiters May be null
     * @param stronglyReservedWords May be null
     * @param weaklyReservedWords May be null
     * @param dynamicTokenRegex May be null
     */
    public CompilerBuilder setComponents(
        LexicalAnalyserFactory lexicalAnalyserFactory,
        SyntaxAnalyserFactory syntaxAnalyserFactory,
        CodeGeneratorFactory codeGeneratorFactory,
        Grammar grammar,
        RuleConvertor ruleConvertor,
        String[] whitespaceDelimiters,
        String[] stronglyReservedWords,
        String[] weaklyReservedWords,
        DynamicTokenRegex[] dynamicTokenRegex
    ) {
        setLexicalAnalyser(lexicalAnalyserFactory);
        setSyntaxAnalyser(syntaxAnalyserFactory);
        setCodeGenerator(codeGeneratorFactory);
        setGrammar(grammar);
        setRuleConvertor(ruleConvertor);
        setWhitespaceDelimiters(whitespaceDelimiters);
        setStronglyReservedWords(stronglyReservedWords);
        setWeaklyReservedWords(weaklyReservedWords);
        setDynamicTokenRegex(dynamicTokenRegex);

        return this;
    }
    

    public CompilerBuilder setLexicalAnalyser(LexicalAnalyserFactory factory) {
        this.lexicalAnalyserFactory = factory;
        return this;
    }

    public CompilerBuilder setSyntaxAnalyser(SyntaxAnalyserFactory factory) {
        this.syntaxAnalyserFactory = factory;
        return this;
    }

    public CompilerBuilder setCodeGenerator(CodeGeneratorFactory factory) {
        this.codeGeneratorFactory = factory;
        return this;
    }


    public CompilerBuilder setGrammar(Grammar grammar) {
        this.grammar = grammar;
        return this;
    }

    public CompilerBuilder setRuleConvertor(RuleConvertor ruleConvertor) {
        this.ruleConvertor = ruleConvertor;
        return this;
    }

    
    public CompilerBuilder setWhitespaceDelimiters(String[] whitespaceDelimiters) {
        this.whitespaceDelimiters = whitespaceDelimiters;
        return this;
    }

    public CompilerBuilder setStronglyReservedWords(String[] stronglyReservedWords) {
        this.stronglyReservedWords = stronglyReservedWords;
        return this;
    }

    public CompilerBuilder setWeaklyReservedWords(String[] weaklyReservedWords) {
        this.weaklyReservedWords = weaklyReservedWords;
        return this;
    }

    public CompilerBuilder setDynamicTokenRegex(DynamicTokenRegex[] dynamicTokenRegex) {
        this.dynamicTokenRegex = dynamicTokenRegex;
        return this;
    }


    public Compiler createCompiler() {
        checkForCompleteBuild();

        if(whitespaceDelimiters == null) whitespaceDelimiters = new String[]{};
        if(stronglyReservedWords == null) stronglyReservedWords = new String[]{};
        if(weaklyReservedWords == null) weaklyReservedWords = new String[]{};
        if(dynamicTokenRegex == null) dynamicTokenRegex = new DynamicTokenRegex[]{};

        GrammarParts parts = grammar.getParts();

        LexicalAnalyser lexicalAnalyser = lexicalAnalyserFactory.produceAnalyser(
            whitespaceDelimiters,
            stronglyReservedWords,
            weaklyReservedWords,
            dynamicTokenRegex
        );
        SyntaxAnalyser syntaxAnalyser = syntaxAnalyserFactory.produceAnalyser(parts);
        CodeGenerator codeGenerator = codeGeneratorFactory.produceGenerator(ruleConvertor);

        return new Compiler(
            lexicalAnalyser,
            syntaxAnalyser,
            codeGenerator
        );
    }

    private void checkForCompleteBuild() {
        List<String> msgParts = new ArrayList<>();

        if(lexicalAnalyserFactory == null) msgParts.add("lexical analyser factory");
        if(syntaxAnalyserFactory == null) msgParts.add("syntax analyser factory");
        if(codeGeneratorFactory == null) msgParts.add("code generator factory");
        if(grammar == null) msgParts.add("grammar");
        if(ruleConvertor == null) msgParts.add("rule convertor");
        //Note: Allowing null lexical components
        
        if(msgParts.size() == 0) return; //No error

        if(msgParts.size() == 1) throw new ParameterError(msgParts.get(0) + " not provided");

        String msg = msgParts.get(0);

        for(int i = 1; i < msgParts.size() - 1; i++) {
            msg += ", " + msgParts.get(i);
        }

        msg += " and " + msgParts.get(msgParts.size() - 1) + " not provided";

        throw new ParameterError(msg);
    }
}
