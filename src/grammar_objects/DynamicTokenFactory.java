package grammar_objects;

public class DynamicTokenFactory {
    
    public <T extends Token> T create(
        String type,
        String grammaticalName,
        String instanceName,
        int lineNum,
        int columNum
    ) {
        //TODO
        throw new RuntimeException(type + " type not found");
    }
}
