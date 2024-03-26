package builders;

import java.util.*;

import grammar_objects.*;
import lexical_analysis.*;
import syntax_analysis.SyntaxAnalyser;
import code_generation.CodeGenerator;

public class CompilerBuilder { //TODO: Extend to allow GrammarBundles to be used (also create builder for bundles), bundles should have everything needed for conversion
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
     * @param grammar
     * @param ruleConvertor
     * @param whitespaceDelimiters May be null
     * @param stronglyReservedWords May be null
     * @param weaklyReservedWords May be null
     * @param dynamicTokenRegex May be null
     */
    public void setComponents(
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
    }
    

    public void setLexicalAnalyser(LexicalAnalyserFactory factory) {
        this.lexicalAnalyserFactory = factory;
    }

    public void setSyntaxAnalyser(SyntaxAnalyserFactory factory) {
        this.syntaxAnalyserFactory = factory;
    }

    public void setCodeGenerator(CodeGeneratorFactory factory) {
        this.codeGeneratorFactory = factory;
    }


    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public void setRuleConvertor(RuleConvertor ruleConvertor) {
        this.ruleConvertor = ruleConvertor;
    }

    
    public void setWhitespaceDelimiters(String[] whitespaceDelimiters) {
        this.whitespaceDelimiters = whitespaceDelimiters;
    }

    public void setStronglyReservedWords(String[] stronglyReservedWords) {
        this.stronglyReservedWords = stronglyReservedWords;
    }

    public void setWeaklyReservedWords(String[] weaklyReservedWords) {
        this.weaklyReservedWords = weaklyReservedWords;
    }

    public void setDynamicTokenRegex(DynamicTokenRegex[] dynamicTokenRegex) {
        this.dynamicTokenRegex = dynamicTokenRegex;
    }


    public Compiler createCompiler() {
        checkForCompleteBuild();

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
        // if(whitespaceDelimiters == null) msgParts.add("whitespace delimiters"); //Allow null
        // if(stronglyReservedWords == null) msgParts.add("strongly reserved words");
        // if(weaklyReservedWords == null) msgParts.add("weakly reserved words");
        // if(dynamicTokenRegex == null) msgParts.add("dynamic token regex");
        
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
