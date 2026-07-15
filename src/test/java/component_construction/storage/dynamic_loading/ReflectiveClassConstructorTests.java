package component_construction.storage.dynamic_loading;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import test_aids.storage_entities.TestDynamicObject;
import test_aids.storage_entities.TestDynamicObject.*;

public class ReflectiveClassConstructorTests {

    protected String TestDynamicObjectNamespace = TestDynamicObject.class.getPackageName();
    protected String nonExistantTestDynamicObjectPath = TestDynamicObjectNamespace + ".NonExistantTestDynamicObject";
    protected String nonExistantStaticInnerTestDynamicObjectPath = TestDynamicObjectNamespace + ".NonExistantTestDynamicObject$StaticInnerTestDynamicObject";
    protected String nonExistantInnerTestDynamicObjectPath = TestDynamicObjectNamespace + ".NonExistantTestDynamicObject$InnerTestDynamicObject";

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
    public void construct_noParameters_nonExistantClass() throws ClassNotFoundException {
        assertThrows(ClassNotFoundException.class, () -> 
            new ReflectiveClassConstructor()
            .construct(
                nonExistantTestDynamicObjectPath,
                TestDynamicObject.class
            )
        );
    }

    @Test
    public void construct_noParameters_nonExistantStaticInnerClass() throws ClassNotFoundException {
        assertThrows(ClassNotFoundException.class, () -> 
            new ReflectiveClassConstructor()
            .construct(
                nonExistantStaticInnerTestDynamicObjectPath,
                TestDynamicObject.class
            )
        );
    }

    @Test
    public void construct_noParameters_nonExistantInnerClass() throws ClassNotFoundException {
        assertThrows(ClassNotFoundException.class, () -> 
            new ReflectiveClassConstructor()
            .construct(
                nonExistantInnerTestDynamicObjectPath,
                TestDynamicObject.class
            )
        );
    }

    @Test
    public void construct_oneParameter_class_intParam() throws ClassNotFoundException {
        TestDynamicObject expectedObject = new TestDynamicObject();
        expectedObject.a = 1;

        TestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                TestDynamicObject.class.getName(),
                TestDynamicObject.class,
                new Class<?>[] {int.class},
                new Object[] {expectedObject.a}
            );

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter_staticInnerClass_intParam() throws ClassNotFoundException {      
        StaticInnerTestDynamicObject expectedObject = new StaticInnerTestDynamicObject();
        expectedObject.e = 1;

        StaticInnerTestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                StaticInnerTestDynamicObject.class.getName(),
                StaticInnerTestDynamicObject.class,
                new Class<?>[] {int.class},
                new Object[] {expectedObject.e}
            );

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter_innerClass_intParam() throws ClassNotFoundException {        
        TestDynamicObject parentObject = new TestDynamicObject();
        InnerTestDynamicObject expectedObject = parentObject.new InnerTestDynamicObject();
        expectedObject.c = 1;
        
        InnerTestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                InnerTestDynamicObject.class.getName(),
                InnerTestDynamicObject.class,
                new Class<?>[] {
                    parentObject.getClass(),
                    int.class
                },
                new Object[] {
                    parentObject,
                    expectedObject.c
                }
            );

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter_class_stringParam() throws ClassNotFoundException {
        TestDynamicObject expectedObject = new TestDynamicObject();
        expectedObject.b = "Test";

        TestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                TestDynamicObject.class.getName(),
                TestDynamicObject.class,
                new Class<?>[] {String.class},
                new Object[] {expectedObject.b}
            );

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter_staticInnerClass_stringParam() throws ClassNotFoundException {        
        StaticInnerTestDynamicObject expectedObject = new StaticInnerTestDynamicObject();
        expectedObject.f = "Test";

        StaticInnerTestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                StaticInnerTestDynamicObject.class.getName(),
                StaticInnerTestDynamicObject.class,
                new Class<?>[] {String.class},
                new Object[] {expectedObject.f}
            );

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter_innerClass_stringParam() throws ClassNotFoundException {        
        TestDynamicObject parentObject = new TestDynamicObject();
        InnerTestDynamicObject expectedObject = parentObject.new InnerTestDynamicObject();
        expectedObject.d = "Test";
        
        InnerTestDynamicObject actualObject = new ReflectiveClassConstructor()
            .construct(
                InnerTestDynamicObject.class.getName(),
                InnerTestDynamicObject.class,
                new Class<?>[] {
                    parentObject.getClass(),
                    String.class
                },
                new Object[] {
                    parentObject,
                    expectedObject.d
                }
            );

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void construct_oneParameter_class_falseBooleanType() throws ClassNotFoundException {
        TestDynamicObject expectedObject = new TestDynamicObject();
        expectedObject.a = 1;

        assertThrows(IllegalArgumentException.class, () ->
            new ReflectiveClassConstructor()
            .construct(
                TestDynamicObject.class.getName(),
                TestDynamicObject.class,
                new Class<?>[] {boolean.class},
                new Object[] {expectedObject.a}
            )
        );
    }

    @Test
    public void construct_oneParameter_staticInnerClass_falseBooleanType() throws ClassNotFoundException {      
        StaticInnerTestDynamicObject expectedObject = new StaticInnerTestDynamicObject();
        expectedObject.e = 1;

        assertThrows(IllegalArgumentException.class, () ->
            new ReflectiveClassConstructor()
            .construct(
                StaticInnerTestDynamicObject.class.getName(),
                StaticInnerTestDynamicObject.class,
                new Class<?>[] {boolean.class},
                new Object[] {expectedObject.e}
            )
        );
    }

    @Test
    public void construct_oneParameter_innerClass_falseBooleanType() throws ClassNotFoundException {        
        TestDynamicObject parentObject = new TestDynamicObject();
        InnerTestDynamicObject expectedObject = parentObject.new InnerTestDynamicObject();
        expectedObject.c = 1;
        
        assertThrows(IllegalArgumentException.class, () ->
            new ReflectiveClassConstructor()
            .construct(
                InnerTestDynamicObject.class.getName(),
                InnerTestDynamicObject.class,
                new Class<?>[] {
                    parentObject.getClass(),
                    boolean.class
                },
                new Object[] {
                    parentObject,
                    expectedObject.c
                }
            )
        );
    }

    @Test
    public void construct_oneParameter_class_falseBooleanParam() throws ClassNotFoundException {
        TestDynamicObject expectedObject = new TestDynamicObject();
        expectedObject.a = 1;

        assertThrows(IllegalArgumentException.class, () ->
            new ReflectiveClassConstructor()
            .construct(
                TestDynamicObject.class.getName(),
                TestDynamicObject.class,
                new Class<?>[] {int.class},
                new Object[] {false}
            )
        );
    }

    @Test
    public void construct_oneParameter_staticInnerClass_falseBooleanParam() throws ClassNotFoundException {      
        StaticInnerTestDynamicObject expectedObject = new StaticInnerTestDynamicObject();
        expectedObject.e = 1;

        assertThrows(IllegalArgumentException.class, () ->
            new ReflectiveClassConstructor()
            .construct(
                StaticInnerTestDynamicObject.class.getName(),
                StaticInnerTestDynamicObject.class,
                new Class<?>[] {int.class},
                new Object[] {false}
            )
        );
    }

    @Test
    public void construct_oneParameter_innerClass_falseBooleanParam() throws ClassNotFoundException {        
        TestDynamicObject parentObject = new TestDynamicObject();
        InnerTestDynamicObject expectedObject = parentObject.new InnerTestDynamicObject();
        expectedObject.c = 1;
        
        assertThrows(IllegalArgumentException.class, () ->
            new ReflectiveClassConstructor()
            .construct(
                InnerTestDynamicObject.class.getName(),
                InnerTestDynamicObject.class,
                new Class<?>[] {
                    parentObject.getClass(),
                    int.class
                },
                new Object[] {
                    parentObject,
                    false
                }
            )
        );
    }

    @Test
    public void construct_manyParameters() {
        //TODO
    }

    //TODO: Static and regular inner classes

    //TODO: Non existant class attempts
    //TODO: Load as subclass type
}
