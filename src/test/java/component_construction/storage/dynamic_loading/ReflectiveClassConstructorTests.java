package component_construction.storage.dynamic_loading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import test_aids.storage_entities.TestDynamicObject;
import test_aids.storage_entities.TestDynamicObject.*;

public class ReflectiveClassConstructorTests {

    @Test
    public void construct_noParameters_class() throws ClassNotFoundException {
        TestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                TestDynamicObject.class.getName(),
                TestDynamicObject.class
            );
        TestDynamicObject expectedObject = new TestDynamicObject();

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_noParameters_staticInnerClass() throws ClassNotFoundException {        
        StaticInnerTestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                StaticInnerTestDynamicObject.class.getName(),
                StaticInnerTestDynamicObject.class
            );
        StaticInnerTestDynamicObject expectedObject = new StaticInnerTestDynamicObject();

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_noParameters_innerClass() throws ClassNotFoundException {        
        TestDynamicObject parentObject = new TestDynamicObject();
        
        InnerTestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                InnerTestDynamicObject.class.getName(),
                InnerTestDynamicObject.class,
                new Class<?>[] {parentObject.getClass()},
                new Object[] {parentObject}
            );
        InnerTestDynamicObject expectedObject = parentObject.new InnerTestDynamicObject();

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter() {
        //TODO
    }

    //TODO: static and regular inner classes

    @Test
    public void construct_manyParameters() {
        //TODO
    }

    //TODO: static and regular inner classes

    //TODO: non existant class attempts
    //TODO: load as subclass type
}
