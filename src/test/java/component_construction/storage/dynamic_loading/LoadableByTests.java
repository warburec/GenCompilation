package component_construction.storage.dynamic_loading;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import test_aids.storage_entities.loadableby_entities.*;

public class LoadableByTests {

    @Test
    @SuppressWarnings("unchecked")
    public void getTargetLoader() {
        Class<LoadableBy<Loader<LoadableObject>>> loadableBy = (Class<LoadableBy<Loader<LoadableObject>>>)(Class<?>)LoadableObject.class;
        Class<Loader<LoadableObject>> actualLoader = LoadableBy.getTargetLoader(loadableBy);

        assertEquals(StandardLoader.class, actualLoader);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getTargetLoader_GenericLoader() {
        Class<LoadableBy<Loader<Object>>> loadableBy = (Class<LoadableBy<Loader<Object>>>)(Class<?>)GenericLoader.class;
        
        assertThrows(IllegalArgumentException.class, () -> LoadableBy.getTargetLoader(loadableBy));
    }

    // Additional interfaces of loadable class
    //  Another interface without generics - implements Runnable, LoadablyBy<ThisClass>
    //  Another interface with generics - implements AnotherInterface<String>, LoadablyBy<ThisClass>

    // Nested Loaders?
}
