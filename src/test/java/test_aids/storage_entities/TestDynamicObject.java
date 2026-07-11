package test_aids.storage_entities;

public class TestDynamicObject {
    public TestDynamicObject() {}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestDynamicObject)) return false;
        return true;
    }

    public class InnerTestDynamicObject {
        public InnerTestDynamicObject() {}

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof InnerTestDynamicObject)) return false;
            return true;
        }
    }

    public static class StaticInnerTestDynamicObject {
        public StaticInnerTestDynamicObject() {}

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StaticInnerTestDynamicObject)) return false;
            return true;
        }
    }
}
