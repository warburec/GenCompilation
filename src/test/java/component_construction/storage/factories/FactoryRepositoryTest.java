package component_construction.storage.factories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FactoryRepositoryTest {
    protected Factory<String> testFactory1 = (loadValue) -> loadValue.getValue().toString();
    protected Factory<String> testFactory2 = (loadValue) -> loadValue.getValue().toString() + "2";

    @Test
    public void addFactory() {
        FactoryRepository<String> repository = new FactoryRepository<String>();

        FactoryRepository<String> actualRepository = assertDoesNotThrow(
            () -> repository.addFactory("testFactory1", testFactory1)
        );

        assertEquals(repository, actualRepository);
    }

    @Test
    public void containsKey_key1() {
        FactoryRepository<String> repository = new FactoryRepository<String>();
        repository.addFactory("testFactory1", testFactory1);
        repository.addFactory("testFactory2", testFactory2);

        assertTrue(repository.containsKey("testFactory1"));
    }

    @Test
    public void containsKey_key2() {
        FactoryRepository<String> repository = new FactoryRepository<String>();
        repository.addFactory("testFactory1", testFactory1);
        repository.addFactory("testFactory2", testFactory2);

        assertTrue(repository.containsKey("testFactory2"));
    }

    //TODO: Consider, why not use a factory?
    // instantiate
}
