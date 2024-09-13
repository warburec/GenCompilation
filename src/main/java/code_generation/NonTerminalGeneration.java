package code_generation;

public class NonTerminalGeneration implements CodeElement {

    private String code;

    public NonTerminalGeneration(String code) {
        this.code = code;
    }

    @Override
    public String getGeneration() {
        return code;
    }
    
}
