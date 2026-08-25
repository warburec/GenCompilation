package component_construction.storage.exceptions;

import java.util.Collection;
import java.util.stream.Collectors;

public class MissingKeyException extends RuntimeException {
    public Collection<String> missingKeys;

    public MissingKeyException(Collection<String> missingKeys) {
        super();
        this.missingKeys = missingKeys;
    }

    @Override
    public String getMessage() {
        if (missingKeys.size() == 1)
            return "The key \"" + missingKeys.iterator().next() + "\" is missing";

        String missingKeysString = missingKeys
            .stream()
            .map(value -> "\"" + value + "\"")
            .collect(Collectors.joining(", "));
        
        return "The keys " + missingKeysString + " are missing";
    }
}
