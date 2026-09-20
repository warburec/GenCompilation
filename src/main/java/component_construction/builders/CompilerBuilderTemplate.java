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
     * @param lexicalAnalyserFactory A factory which produces lexical analysers
     * @param syntaxAnalyserFactory A factory which produces syntax analysers
     * @param codeGeneratorFactory A factory which produces code generators
     * @param grammarBundle A {@code GrammarBundle} for the grammar to be compiled
     * @return This object for method chaining
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
     * @param lexicalAnalyserFactory A factory which produces lexical analysers
     * @param syntaxAnalyserFactory A factory which produces syntax analysers
     * @param codeGeneratorFactory A factory which produces code generators
     * @param grammar The {@code grammar} to be used for compilation
     * @param ruleConvertor The {@code RuleConvertor} to produce output code from compiled input sentences
     * @param whitespaceDelimiters An array of strings to be conidered as whitespace in compiled sentences. May be null
     * @param stronglyReservedWords An array of strings which may only be used by reserved words within the provided grammar. May be null
     * @param weaklyReservedWords An array of strings to be used by reserved words within the provided grammar, they may appear as part of other tokens. May be null
     * @param dynamicTokenRegex An array of {@code DynamicTokenRegex} for grammar tokens which may take different forms according to provided regex. May be null
     * @return This object for method chaining
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
    

    /**
     * Sets the lexical analyser for the produced compiler
     * @param factory A factory which produces lexical analysers
     * @return This object for method chaining
     */
    public B setLexicalAnalyser(LexicalAnalyserFactory factory) {
        this.lexicalAnalyserFactory = factory;
        return getThis();
    }

    /**
     * Sets the syntax analyser for the produced compiler
     * @param factory A factory which produces syntax analysers
     * @return This object for method chaining
     */
    public B setSyntaxAnalyser(SyntaxAnalyserFactory factory) {
        this.syntaxAnalyserFactory = factory;
        return getThis();
    }

    /**
     * Sets the code generator for the produced compiler
     * @param factory A factory which produces code generators
     * @return This object for method chaining
     */
    public B setCodeGenerator(CodeGeneratorFactory factory) {
        this.codeGeneratorFactory = factory;
        return getThis();
    }


    /**
     * Sets the grammar for the produced compiler
     * @param grammar The {@code grammar} to be used for compilation
     * @return This object for method chaining
     */
    public B setGrammar(Grammar grammar) {
        this.grammar = grammar;
        return getThis();
    }

    /**
     * Sets the rule convertor for the produced compiler
     * @param ruleConvertor The {@code RuleConvertor} to produce output code from compiled input sentences
     * @return This object for method chaining
     */
    public B setRuleConvertor(RuleConvertor ruleConvertor) {
        this.ruleConvertor = ruleConvertor;
        return getThis();
    }


    /**
     * Sets the designated whitespace delimiters for compilation
     * @param whitespaceDelimiters An array of strings to be conidered as whitespace in compiled sentences
     * @return This object for method chaining
     */
    public B setWhitespaceDelimiters(String[] whitespaceDelimiters) {
        this.whitespaceDelimiters = whitespaceDelimiters;
        return getThis();
    }

    /**
     * Sets designated reserved words for compilation. These may not be used within other tokens
     * @param stronglyReservedWords An array of strings which may only be used by reserved words within the provided grammar
     * @return This object for method chaining
     */
    public B setStronglyReservedWords(String[] stronglyReservedWords) {
        this.stronglyReservedWords = stronglyReservedWords;
        return getThis();
    }

    /**
     * Sets reserved words for compilation that may be used as part of other tokens
     * @param weaklyReservedWords An array of strings to be used by reserved words within the provided grammar, they may appear within other tokens
     * @return This object for method chaining
     */
    public B setWeaklyReservedWords(String[] weaklyReservedWords) {
        this.weaklyReservedWords = weaklyReservedWords;
        return getThis();
    }

    /**
     * Sets the dynaic tokens for comiplation
     * @param dynamicTokenRegex An array of {@code DynamicTokenRegex} for grammar tokens which may take different forms according to provided regex
     * @return This object for method chaining
     */
    public B setDynamicTokenRegex(DynamicTokenRegex[] dynamicTokenRegex) {
        this.dynamicTokenRegex = dynamicTokenRegex;
        return getThis();
    }


    /**
     * Creates a new compiler according to the components provided to this builder
     * @return The produced compiler
     */
    public abstract C createCompiler();


    /**
     * A simple helper method to be filled with "{@code return this;}" by subclasses of this builder
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
