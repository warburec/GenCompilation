package component_construction.builders;

import java.util.*;

import grammar_objects.*;
import lexical_analysis.*;
import component_construction.ParameterError;
import component_construction.bundles.GrammarBundle;
import component_construction.custom_components.*;
import component_construction.factories.code_generation.CodeGeneratorFactory;
import component_construction.factories.lexical_analysis.LexicalAnalyserFactory;
import component_construction.factories.syntax_analysis.SyntaxAnalyserFactory;

/**
 * An template for builders of CustomCompilers
 * @param <B> The type of the current implementing subclass of this template
 * @param <C> The type of CustomCompiler that will be built by the implemented builder
 */
public abstract class CompilerBuilderTemplate <B extends CompilerBuilderTemplate<B, C>, C extends CustomCompiler> {
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
    public B setComponents(
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
    public B setComponents(
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

        return getThis();
    }
    

    public B setLexicalAnalyser(LexicalAnalyserFactory factory) {
        this.lexicalAnalyserFactory = factory;
        return getThis();
    }

    public B setSyntaxAnalyser(SyntaxAnalyserFactory factory) {
        this.syntaxAnalyserFactory = factory;
        return getThis();
    }

    public B setCodeGenerator(CodeGeneratorFactory factory) {
        this.codeGeneratorFactory = factory;
        return getThis();
    }


    public B setGrammar(Grammar grammar) {
        this.grammar = grammar;
        return getThis();
    }

    public B setRuleConvertor(RuleConvertor ruleConvertor) {
        this.ruleConvertor = ruleConvertor;
        return getThis();
    }

    
    public B setWhitespaceDelimiters(String[] whitespaceDelimiters) {
        this.whitespaceDelimiters = whitespaceDelimiters;
        return getThis();
    }

    public B setStronglyReservedWords(String[] stronglyReservedWords) {
        this.stronglyReservedWords = stronglyReservedWords;
        return getThis();
    }

    public B setWeaklyReservedWords(String[] weaklyReservedWords) {
        this.weaklyReservedWords = weaklyReservedWords;
        return getThis();
    }

    public B setDynamicTokenRegex(DynamicTokenRegex[] dynamicTokenRegex) {
        this.dynamicTokenRegex = dynamicTokenRegex;
        return getThis();
    }

    
    public abstract C createCompiler();


    /**
     * A simple helper method to be filled with "return this;" by subclasses of this builder
     * @return The current builder object "this"
     */
    protected abstract B getThis();

    protected void checkForCompleteBuild() {
        List<String> msgParts = new ArrayList<>();

        if(lexicalAnalyserFactory == null) msgParts.add("lexical analyser factory");
        if(syntaxAnalyserFactory == null) msgParts.add("syntax analyser factory");
        if(codeGeneratorFactory == null) msgParts.add("code generator factory");
        if(grammar == null) msgParts.add("grammar");
        if(ruleConvertor == null) msgParts.add("rule convertor");
        // Note: Allowing null lexical components
        
        if(msgParts.size() == 0) return; // No error
        if(msgParts.size() == 1) throw new ParameterError(msgParts.get(0) + " not provided");

        String msg = msgParts.get(0);

        for(int i = 1; i < msgParts.size() - 1; i++) {
            msg += ", " + msgParts.get(i);
        }

        msg += " and " + msgParts.get(msgParts.size() - 1) + " not provided";

        throw new ParameterError(msg);
    }
}
