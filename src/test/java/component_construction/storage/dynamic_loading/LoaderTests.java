package component_construction.storage.dynamic_loading;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import component_construction.storage.exceptions.MissingEmptyConstructorException;
import test_aids.storage_entities.*;

public class LoaderTests {

    @Test
    @SuppressWarnings("unchecked")
    public void construct() {
        Class<Loader<TestObject>> loaderClass = (Class<Loader<TestObject>>)(Class<?>)TestObjectLoader.class;

        assertDoesNotThrow(() -> Loader.construct(loaderClass));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void construct_IncorrectlyFormedLoader() {
        Class<Loader<Boolean>> loaderClass = (Class<Loader<Boolean>>)(Class<?>)IncorrectlyFormedLoader.class;

        assertThrows(MissingEmptyConstructorException.class, () -> Loader.construct(loaderClass));
    }
}
