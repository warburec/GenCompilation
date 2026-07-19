package component_construction.storage.dynamic_loading;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import test_aids.storage_entities.*;

public class LoaderTests {
    
    @Test
    @SuppressWarnings("unchecked")
    public void construct() {
        Loader<TestObject> expectedLoader = new TestObjectLoader();
        Class<Loader<TestObject>> loaderClass = (Class<Loader<TestObject>>)expectedLoader.getClass();

        assertDoesNotThrow(() -> Loader.construct(loaderClass));
    }

}
