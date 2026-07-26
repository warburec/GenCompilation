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

    @Test
    @SuppressWarnings("unchecked")
    public void getTargetLoader_additionalInterface() {
        Class<LoadableBy<Loader<TestLoadableObject>>> loadableBy = (Class<LoadableBy<Loader<TestLoadableObject>>>)(Class<?>)TestLoadableObject.class;
        Class<Loader<TestLoadableObject>> actualLoader = LoadableBy.getTargetLoader(loadableBy);

        assertEquals(StandardLoader.class, actualLoader);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getTargetLoader_additionalGenericInterface() {
        Class<LoadableBy<Loader<GenericTestLoadableObject<?>>>> loadableBy = (Class<LoadableBy<Loader<GenericTestLoadableObject<?>>>>)(Class<?>)GenericTestLoadableObject.class;
        Class<Loader<GenericTestLoadableObject<?>>> actualLoader = LoadableBy.getTargetLoader(loadableBy);

        assertEquals(StandardLoader.class, actualLoader);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getTargetLoader_additionalConcretisedGenericInterface() {
        Class<LoadableBy<Loader<ConcretisedGenericTestLoadableObject<?>>>> loadableBy = (Class<LoadableBy<Loader<ConcretisedGenericTestLoadableObject<?>>>>)(Class<?>)ConcretisedGenericTestLoadableObject.class;
        Class<Loader<ConcretisedGenericTestLoadableObject<?>>> actualLoader = LoadableBy.getTargetLoader(loadableBy);

        assertEquals(StandardLoader.class, actualLoader);
    }

    // Nested Loaders?
}
