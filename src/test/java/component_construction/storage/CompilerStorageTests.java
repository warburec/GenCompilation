package component_construction.storage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import storage.exceptions.NullStorageObjectException;

public class CompilerStorageTests {

    @Test
    public void store_null() {
        CompilerStorage storage = new CompilerStorage();
        StorableCustomCompiler compiler = null;


        assertThrows(NullStorageObjectException.class, () -> storage.store(compiler));
    }

    //store(StorableCustomCompiler)

    //load() valid format
    //load() incomplete format
    //load() incrrect format
}
